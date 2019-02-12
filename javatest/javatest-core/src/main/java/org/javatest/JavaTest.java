package org.javatest;

import org.javatest.assertions.Assertion;
import org.javatest.assertions.AssertionResult;
import org.javatest.tests.*;
import org.javatest.tests.TestResult;
import org.javatest.tests.TestResults;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class JavaTest {

    public static final String SEPARATOR = System.lineSeparator();

    public static TestResults run(TestProvider testProvider) {
        return run(testProvider.testStream());
    }

    public static TestResults run(Stream<Test> tests) {
        var result = tests
                .map(JavaTest::runTest)
                .reduce(TestResults.init(), TestResults::addResult, TestResults::combine);
        // TODO allow by test logging, perhaps with some kind of observer.
        result.testLogs.forEach(System.out::println);
        System.out.println(Colour.WHITE.getCode());
        System.out.println(result.totalsLog());
        return result;
    }

    private static TestResult runTest(Test test) {
        // TODO allow a test to add to the log. Ideally immutable :/ probably have to be some kind of builder per test case.
        var result = safeRunTest(test.test);
        var colour = getColour(result);
        var log = colour.getCode() + test.name + Colour.resetCode() + SEPARATOR + "\t" + result.description;
        return new TestResult(result, log);
    }

    private static AssertionResult safeRunTest(CheckedSupplier<Assertion> test) {
        try {
            return test.get().run();
        } catch (AssertionError e) {
            return AssertionResult.failed(e);
        } catch (Throwable e) {
            return AssertionResult.failed(e);
        }
    }

    public static Colour getColour(AssertionResult result) {
        if (result.pending) {
            return Colour.YELLOW;
        } else if (result.holds) {
            return Colour.GREEN;
        } else {
            return Colour.RED;
        }
    }
    public enum Colour {

        RED("\u001B[31m"), YELLOW("\u001B[33m"), GREEN("\u001B[32m"), INVISIBLE("\033[37m"), WHITE(resetCode());

        private final String colourCode;

        Colour(final String colourCode) {
            this.colourCode = colourCode;
        }

        public String getCode() {
            return colourCode;
        }

        public static String resetCode() {
            return "\u001B[0m";
        }

    }

    public static Test test(String name, CheckedSupplier<Assertion> test) {
        return Test.test(name, test, Collections.emptyList());
    }

    public static Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return Test.test(name, test, tags);
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

