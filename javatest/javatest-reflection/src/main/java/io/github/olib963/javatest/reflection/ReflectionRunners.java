package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.TestRunners;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.reflection.internal.Utils;
import io.github.olib963.javatest.reflection.internal.Package;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.testableRunner;

public class ReflectionRunners implements TestRunners {
    private final File rootDirectory;
    private final ClassLoader classLoader;
    private final Collection<Predicate<String>> packageNameFilters;
    private ReflectionRunners(File rootDirectory, ClassLoader classLoader, Collection<Predicate<String>> packageNameFilters) {
        this.rootDirectory = rootDirectory;
        this.classLoader = classLoader;
        this.packageNameFilters = packageNameFilters;
    }

    public static ReflectionRunners forTestSourceRoot(File rootDirectory, ClassLoader classLoader) {
        return new ReflectionRunners(rootDirectory, classLoader, Collections.emptyList());
    }

    public ReflectionRunners filter(Predicate<String> packagePredicate) {
        var newFilters = new ArrayList<>(packageNameFilters);
        newFilters.add(packagePredicate);
        return new ReflectionRunners(rootDirectory, classLoader, newFilters);
    }

    @Override
    public Collection<TestRunner> runners() {
        var classes = Utils.allFilesIn(rootDirectory)
                .flatMap(f -> allPackages(f, ""))
                .filter(Package -> packageNameFilters.stream().allMatch(p -> p.test(Package.packageName)))
                .flatMap(Package::classes);

        var aggregated = classes.reduce(Aggregate.empty(), (aggregate, className) ->{
            try {
                var testClass = classLoader.loadClass(className);
                var zeroArgConstructor = testClass.getConstructor(null);
                if(TestSuiteClass.class.isAssignableFrom(testClass)) {
                    return aggregate.withSuite((TestSuiteClass) zeroArgConstructor.newInstance());
                } else if(TestRunners.class.isAssignableFrom(testClass)) {
                    return aggregate.withRunners((TestRunners) zeroArgConstructor.newInstance());
                } else {
                    return aggregate;
                }
            } catch (ClassNotFoundException e) {
                // TODO return a failure for class not found. In theory this should happen if a different class loader is used?
                e.printStackTrace();
                throw new RuntimeException(e);
            }catch (NoSuchMethodException e) {
                // No zero argument constructor - move on to next class.
                return aggregate;
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                var message = "Could not create instance of " + className + " because:\n" + Utils.flattenMessages(e);
                return aggregate.withClassFailure(message);
            }
        }, Aggregate::compose);
        var singleRunner = testableRunner(aggregated.classes);

        TestRunner failingRunner = () -> aggregated.failedClasses.stream()
                .reduce(TestResults.empty(), TestResults::failBecause , TestResults::combine);

        return Stream.concat(
                Stream.of(singleRunner, failingRunner),
                aggregated.runners.stream().flatMap(r -> r.runners().stream())
        ).collect(Collectors.toList());
    }

    private Stream<Package> allPackages(File file, String currentPackage) {
        if(file.isDirectory()) {
            var files = Utils.allFilesIn(file);
            var packageName = currentPackage + file.getName() + '.';
            return Stream.concat(
                    Stream.of(new Package(packageName, file)),
                    files.flatMap(f -> allPackages(f, packageName))
            );
        }
        return Stream.empty();
    }

    private static class Aggregate {
        private final Collection<TestSuiteClass> classes;
        private final Collection<TestRunners> runners;
        private final Collection<String> failedClasses;
        private Aggregate(Collection<TestSuiteClass> classes, Collection<TestRunners> runners, Collection<String> failedClasses) {
            this.classes = classes;
            this.runners = runners;
            this.failedClasses = failedClasses;
        }
        private static Aggregate empty() {
            return new Aggregate(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }
        private Aggregate withSuite(TestSuiteClass suiteClass) {
            var newClasses = new ArrayList<>(classes);
            newClasses.add(suiteClass);
            return new Aggregate(newClasses, runners, failedClasses);
        }

        private Aggregate withRunners(TestRunners newRunner) {
            var newRunners = new ArrayList<>(runners);
            newRunners.add(newRunner);
            return new Aggregate(classes, newRunners, failedClasses);
        }

        private Aggregate withClassFailure(String classFailure) {
            var newFailedClasses = new ArrayList<>(failedClasses);
            newFailedClasses.add(classFailure);
            return new Aggregate(classes, runners, newFailedClasses);
        }

        private Aggregate compose(Aggregate other) {
            var newClasses = new ArrayList<>(classes);
            newClasses.addAll(other.classes);
            var newRunners = new ArrayList<>(runners);
            newRunners.addAll(other.runners);
            var newFailedClasses = new ArrayList<>(failedClasses);
            newFailedClasses.addAll(other.failedClasses);
            return new Aggregate(newClasses, newRunners, newFailedClasses);
        }
    }

}
