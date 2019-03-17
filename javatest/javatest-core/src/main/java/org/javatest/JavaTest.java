package org.javatest;

import org.javatest.logging.Colour;
import org.javatest.runners.StreamRunner;
import org.javatest.tests.*;
import org.javatest.tests.TestResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class JavaTest {

    static final String SEPARATOR = System.lineSeparator();

    public static TestRunner testStreamRunner(Stream<Test> tests) {
        return new StreamRunner(tests);
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
        return run(firstRunner);
    }

    private static TestResults run(TestRunner firstRunner) {
        var result = firstRunner.run();
        // TODO allow by test logging, perhaps with some kind of observer. i.e. don't wait to log until they are all finished.
        result.testLogs.forEach(System.out::println);
        System.out.println(Colour.WHITE.getCode());
        System.out.println(result.totalsLog());
        return result;
    }

    // TODO decide how to structure static import if they are to be used.
    public static Test test(String name, CheckedSupplier<Assertion> test) {
        return SimpleTest.test(name, test, Collections.emptyList());
    }

    public static Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return SimpleTest.test(name, test, tags);
    }

    public static Assertion that(boolean asserted, String description) { return Assertion.that(asserted, description); }

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

