package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.javafire.TestRunners;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.reflection.internal.Aggregate;
import io.github.olib963.javatest.reflection.internal.Utils;
import io.github.olib963.javatest.reflection.internal.Package;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.testableRunner;

public class ReflectionRunners implements TestRunners {
    private final Collection<File> rootDirectories;
    private final ClassLoader classLoader;
    // TODO it may be useful to provider a richer wrapper type e.g. Class(Package, Name), Package(Parent, Name)
    private final Collection<Predicate<String>> classNameFilters;

    private ReflectionRunners(Collection<File> rootDirectories, ClassLoader classLoader, Collection<Predicate<String>> classNameFilters) {
        this.rootDirectories = rootDirectories;
        this.classLoader = classLoader;
        this.classNameFilters = classNameFilters;
    }

    public static ReflectionRunners forTestSourceRoots(Collection<File> rootDirectories, ClassLoader classLoader) {
        return new ReflectionRunners(rootDirectories, classLoader, Collections.emptyList());
    }

    public static ReflectionRunners forTestSourceRoot(File rootDirectory, ClassLoader classLoader) {
        return new ReflectionRunners(Collections.singletonList(rootDirectory), classLoader, Collections.emptyList());
    }

    public ReflectionRunners filterClasses(Predicate<String> classFilter) {
        var newFilters = new ArrayList<>(classNameFilters);
        newFilters.add(classFilter);
        return new ReflectionRunners(rootDirectories, classLoader, newFilters);
    }

    @Override
    public Collection<TestRunner> runners() {
        var classes = rootDirectories.stream().flatMap(Package::allPackagesUnderSource).flatMap(Package::classes);
        var aggregated = classes
                .filter(className -> classNameFilters.stream().allMatch(p -> p.test(className)))
                .reduce(Aggregate.empty(), this::aggregate, Aggregate::compose);

        var suiteRunner = testableRunner(aggregated.suites);

        // If there were any failures loading/creating the classes create a failed result.
        TestRunner failingRunner = () -> aggregated.classFailures.stream()
                .reduce(TestResults.empty(), TestResults::failBecause, TestResults::combine);

        return Stream.concat(
                Stream.of(suiteRunner, failingRunner),
                aggregated.runners.stream().flatMap(r -> r.runners().stream())
        ).collect(Collectors.toList());
    }

    private Aggregate aggregate(Aggregate aggregate, String className) {
        try {
            return instantiateIfZeroArgConstructorExists(aggregate, className, classLoader.loadClass(className));
        } catch (ClassNotFoundException e) {
            var message = "Could not load " + className + " because:\n" + Utils.flattenMessages(e);
            return aggregate.withClassFailure(message);
        }
    }

    private Aggregate instantiateIfZeroArgConstructorExists(Aggregate aggregate, String className, Class<?> testClass) {
        try {
            return instantiateIfPossible(aggregate, className, testClass, testClass.getConstructor(null));
        } catch (NoSuchMethodException e) {
            // No zero argument constructor - move on to next class.
            return aggregate;
        }
    }

    private Aggregate instantiateIfPossible(Aggregate aggregate, String className, Class<?> testClass, Constructor<?> constructor) {
        try{
            if (TestSuiteClass.class.isAssignableFrom(testClass)) {
                return aggregate.withSuite((TestSuiteClass) constructor.newInstance());
            } else if (TestRunners.class.isAssignableFrom(testClass)) {
                return aggregate.withRunners((TestRunners) constructor.newInstance());
            } else {
                return aggregate;
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            var message = "Could not create instance of " + className + " because:\n" + Utils.flattenMessages(e);
            return aggregate.withClassFailure(message);
        }
    }

}
