package org.javatest;

import org.javatest.logging.Colour;
import org.javatest.tests.*;
import org.javatest.tests.TestResult;

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
        var result = safeRunTest(test.test());
        var colour = Colour.forResult(result);
        var log = colour.getCode() + test.name() + Colour.resetCode() + SEPARATOR + "\t" + result.description;
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

