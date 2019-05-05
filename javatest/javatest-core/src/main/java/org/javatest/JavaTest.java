package org.javatest;

import org.javatest.assertions.BooleanAssertion;
import org.javatest.assertions.PendingAssertion;
import org.javatest.runners.StreamRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class JavaTest {
    private JavaTest() {}

    // Main entry point of the library
    public static TestResults run(Stream<TestRunner> runners) {
        return runners.map(TestRunner::run).reduce(TestResults.init(), TestResults::combine);
    }

    // Convenience functions
    public static TestResults run(TestRunner firstRunner, TestRunner... moreRunners) {
        return run(Stream.concat(Stream.of(firstRunner), Arrays.stream(moreRunners)));
    }

    public static TestResults runTests(Stream<Test> tests) {
        return run(testStreamRunner(tests));
    }

    // Stream Runner factory methods
    private static final Collection<TestCompletionObserver> DEFAULT_OBSERVER =
            Collections.singletonList(TestCompletionObserver.colourLogger());

    public static TestRunner testStreamRunner(Stream<Test> tests) {
        return new StreamRunner(tests, DEFAULT_OBSERVER);
    }

    public static TestRunner testStreamRunner(Stream<Test> tests, Collection<TestCompletionObserver> observers) {
        return new StreamRunner(tests, observers);
    }

    // Test factory methods
    public static Test test(String name, CheckedSupplier<Assertion> testFunction) {
        return test(name, testFunction, Collections.emptyList());
    }

    public static Test test(String name, CheckedSupplier<Assertion> testFunction, Collection<String> tags) {
        return new Test(name, testFunction, tags);
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

    public static Stream<Test> allTestsFrom(TestSuite... suites) {
        return Arrays.stream(suites).flatMap(TestSuite::testStream);
    }
}

