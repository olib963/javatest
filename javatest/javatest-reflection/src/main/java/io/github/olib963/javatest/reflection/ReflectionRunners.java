package io.github.olib963.javatest.reflection;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.testableRunner;

public class ReflectionRunners implements TestRunners {
    private final File rootDirectory;
    private final ClassLoader classLoader;
    public ReflectionRunners(File rootDirectory, ClassLoader classLoader) {
        this.rootDirectory = rootDirectory;
        this.classLoader = classLoader;
    }

    // TODO a lot of cleanup here.
    @Override
    public Collection<TestRunner> runners() {
        var classes = Utils.allFilesIn(rootDirectory)
                .flatMap(f -> allPackages(f, ""))
                .flatMap(Package::classes);

        var aggregated = classes.map(c -> {
            try {
                return classLoader.loadClass(c);
            } catch (ClassNotFoundException e) {
                // TODO return a failure for class not found. In theory this should happen if a different class loader is used?
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        })
        .reduce(Aggregate.empty(), (a, c) ->{
            try {
                var zeroArgConstructor = c.getConstructor(null);
                if(TestSuiteClass.class.isAssignableFrom(c)) {
                    return a.withSuite((TestSuiteClass) zeroArgConstructor.newInstance());
                } else if(TestRunners.class.isAssignableFrom(c)) {
                    return a.withRunners((TestRunners) zeroArgConstructor.newInstance());
                } else {
                    return a;
                }
            } catch (NoSuchMethodException e) {
                return a;
            } catch (InstantiationException | InvocationTargetException| IllegalAccessException e) {
                // TODO need to handle failure to instantiate class and return it as a failure on the run.
                // For this create a () constructor that throws an exception.
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, Aggregate::compose);
        var singleRunner = testableRunner(aggregated.classes);

        return Stream.concat(
                Stream.of(singleRunner),
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
        private Aggregate(Collection<TestSuiteClass> classes, Collection<TestRunners> runners) {
            this.classes = classes;
            this.runners = runners;
        }
        private static Aggregate empty() {
            return new Aggregate(Collections.emptyList(), Collections.emptyList());
        }
        private Aggregate withSuite(TestSuiteClass suiteClass) {
            var newClasses = new ArrayList<>(classes);
            newClasses.add(suiteClass);
            return new Aggregate(newClasses, runners);
        }

        private Aggregate withRunners(TestRunners newRunner) {
            var newRunners = new ArrayList<>(runners);
            newRunners.add(newRunner);
            return new Aggregate(classes, newRunners);
        }

        private Aggregate compose(Aggregate other) {
            var newClasses = new ArrayList<>(classes);
            newClasses.addAll(other.classes);
            var newRunners = new ArrayList<>(runners);
            newRunners.addAll(other.runners);
            return new Aggregate(newClasses, newRunners);
        }
    }

}
