package io.github.olib963.javatest;

import io.github.olib963.javatest.assertions.BooleanAssertion;
import io.github.olib963.javatest.assertions.PendingAssertion;
import io.github.olib963.javatest.runners.StreamRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
        var results = runners.map(TestRunner::run).reduce(TestResults.init(), TestResults::combine);
        observers.forEach(o -> o.onRunCompletion(results));
        return results;
    }

    // Convenience functions
    public static TestResults run(TestRunner firstRunner, TestRunner... moreRunners) {
        return run(Stream.concat(Stream.of(firstRunner), Arrays.stream(moreRunners)));
    }

    public static TestResults runTests(Stream<Test> tests) {
        return run(testableRunner(tests.map(t -> t)));
    }

    public static TestResults runSuite(TestSuite suite) {
        return run(testableRunner(Stream.of(suite)));
    }

    // Stream Runner factory methods
    private static final Collection<TestCompletionObserver> DEFAULT_OBSERVER =
            Collections.singletonList(TestCompletionObserver.colourLogger());

    public static TestRunner testableRunner(Testable testable) {
        return testableRunner(Stream.of(testable));
    }

    public static TestRunner testableRunner(Stream<Testable> tests) {
        return testableRunner(tests, DEFAULT_OBSERVER);
    }

    public static TestRunner testableRunner(Stream<Testable> tests, Collection<TestCompletionObserver> observers) {
        return new StreamRunner(tests, observers);
    }

    // Test factory methods
    public static Test test(String name, CheckedSupplier<Assertion> testFunction) {
        return new Test(name, testFunction);
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

