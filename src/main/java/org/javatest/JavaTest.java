package org.javatest;

import org.javatest.assertions.Assertion;
import org.javatest.assertions.AssertionResult;
import org.javatest.logging.Colour;
import org.javatest.logging.TestLog;
import org.javatest.logging.TestLogEntry;
import org.javatest.tests.Test;
import org.javatest.tests.TestResult;
import org.javatest.tests.TestResults;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaTest {
    public static TestResults run(Stream<Test> tests) {
        var result = tests
                .map(JavaTest::runTest)
                .reduce(TestResults.init(), TestResults::addResult, TestResults::combine);
        System.out.println(result.testLog.createLogString());
        return result;
    }

    private static TestResult runTest(Test test) {
        var result = safeRunTest(test.test);
        var colour = getColour(result);
        // TODO how to get extra logs from test
        var extraLogs = result.description.stream().map(description -> new TestLogEntry(description, Colour.WHITE)).collect(Collectors.toList());
        return new TestResult(result.holds, new TestLogEntry(test.description, colour, extraLogs));
    }

    private static AssertionResult safeRunTest(Supplier<Assertion> test) {
        try {
            return test.get().run();
        } catch (Exception e) {
            return AssertionResult.failed(e);
        } catch (AssertionError e) {
            return AssertionResult.failed(e);
        }
    }

    private static Colour getColour(AssertionResult result) {
        if (result.pending) {
            return Colour.YELLOW;
        } else if (result.holds) {
            return Colour.GREEN;
        } else {
            return Colour.RED;
        }
    }
}
