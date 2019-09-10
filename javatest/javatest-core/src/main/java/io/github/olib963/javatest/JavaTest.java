package io.github.olib963.javatest;

import io.github.olib963.javatest.assertions.BooleanAssertion;
import io.github.olib963.javatest.assertions.PendingAssertion;
import io.github.olib963.javatest.runners.CollectionRunner;
import io.github.olib963.javatest.runners.StreamRunner;

import java.util.*;
import java.util.stream.Stream;

public class JavaTest {
    private JavaTest() {}

    // Main entry point of the library
    private static final Collection<TestRunCompletionObserver> DEFAULT_RUN_OBSERVER =
            Collections.singletonList(TestRunCompletionObserver.logger());

    public static TestResults run(Stream<TestRunner> runners) {
        return run(runners, DEFAULT_RUN_OBSERVER);
    }

    public static TestResults run(Stream<TestRunner> runners, Collection<TestRunCompletionObserver> observers) {
        var results = runners.map(TestRunner::run).reduce(TestResults.empty(), TestResults::combine);
        observers.forEach(o -> o.onRunCompletion(results));
        return results;
    }

    // Convenience functions
    public static TestResults run(TestRunner firstRunner, TestRunner... moreRunners) {
        return run(Stream.concat(Stream.of(firstRunner), Arrays.stream(moreRunners)));
    }

    public static TestResults runTests(Testable tests) {
        return runTests(List.of(tests));
    }
    public static TestResults runTests(Testable firstTest, Testable... tests) {
        var testables = new ArrayList<Testable>(tests.length + 1);
        testables.add(firstTest);
        testables.addAll(Arrays.asList(tests));
        return runTests(testables);
    }

    public static TestResults runTests(Collection<? extends Testable> tests) {
        return run(testableRunner(tests));
    }

    // Stream Runner factory methods
    private static final Collection<TestCompletionObserver> DEFAULT_OBSERVER =
            Collections.singletonList(TestCompletionObserver.colourLogger());

    public static TestRunner testableRunner(Testable testable) {
        return testableRunner(List.of(testable));
    }

    public static TestRunner testableRunner(Collection<? extends Testable> tests) {
        return testableRunner(tests, DEFAULT_OBSERVER);
    }

    public static TestRunner testableRunner(Collection<? extends Testable> tests, Collection<TestCompletionObserver> observers) {
        return new CollectionRunner(tests, observers);
    }

    public static TestRunner lazyTestableRunner(Stream<? extends Testable> tests) {
        return lazyTestableRunner(tests, DEFAULT_OBSERVER);
    }

    public static TestRunner lazyTestableRunner(Stream<? extends Testable> tests, Collection<TestCompletionObserver> observers) {
        return new StreamRunner(tests, observers);
    }

    // Test factory methods
    public static Testable.Test test(String name, CheckedSupplier<Assertion> testFunction) {
        return new Testable.Test(name, testFunction);
    }

    public static Testable.TestSuite suite(String name, Collection<? extends Testable> testables) {
        return new Testable.TestSuite(name, testables);
    }

    public static Assertion that(boolean asserted, String description) {
        return new BooleanAssertion(asserted, description);
    }

    public static Assertion pending() {
        return pending("Test has not yet been written");
    }

    public static Assertion pending(String reason) {
        return new PendingAssertion(reason);
    }

}

