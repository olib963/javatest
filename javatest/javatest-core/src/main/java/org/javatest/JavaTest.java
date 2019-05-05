package org.javatest;

import org.javatest.runners.StreamRunner;
import org.javatest.tests.SimpleTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

// TODO remove most of the functions and only leave the simplest ones
public class JavaTest {
    private JavaTest() {}

    private static final Collection<TestCompletionObserver> DEFAULT_OBSERVER =
            Collections.singletonList(TestCompletionObserver.colourLogger());

    public static TestRunner testStreamRunner(Stream<Test> tests) {
        return new StreamRunner(tests, DEFAULT_OBSERVER);
    }

    public static TestRunner testStreamRunner(TestProvider testProvider) {
        return testStreamRunner(testProvider.testStream());
    }

    public static TestResults run(Stream<Test> tests) {
        return run(testStreamRunner(tests));
    }

    public static TestResults run(TestProvider testProvider) {
        return run(testStreamRunner(testProvider));
    }

    public static TestResults run(TestRunner firstRunner, TestRunner... moreRunners) {
        return runWithRunners(Stream.concat(Stream.of(firstRunner), Arrays.stream(moreRunners)));
    }

    // TODO how do we get around javas stupid type erasure??? I still don't understand why the compiler can't just do that for you.
    public static TestResults runWithRunners(Stream<TestRunner> runners) {
        return runners.map(TestRunner::run).reduce(TestResults.init(), TestResults::combine);
    }

    // TODO decide how to structure static imports if they are to be used.
    public static Test test(String name, CheckedSupplier<Assertion> test) {
        return SimpleTest.test(name, test, Collections.emptyList());
    }

    public static Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return SimpleTest.test(name, test, tags);
    }

    public static Assertion that(boolean asserted, String description) {
        return Assertion.that(asserted, description);
    }

    public static Assertion pending() {
        return Assertion.pending();
    }

    public static Assertion pending(String reason) {
        return Assertion.pending(reason);
    }

    public static Stream<Test> allTestsFrom(TestProvider... providers) {
        return Arrays.stream(providers).flatMap(TestProvider::testStream);
    }
}

