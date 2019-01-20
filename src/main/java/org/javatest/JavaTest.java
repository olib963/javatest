package org.javatest;

import org.javatest.assertions.Assertion;
import org.javatest.assertions.AssertionResult;
import org.javatest.logging.Colour;
import org.javatest.logging.TestLog;
import org.javatest.logging.TestLogEntry;
import org.javatest.tests.Test;
import org.javatest.tests.TestResult;
import org.javatest.tests.TestResults;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class JavaTest {
    public static TestResults run(Stream<Test> tests) {
        var result = tests
                .map(JavaTest::runTest)
                .reduce(
                        new TestResults(true, TestLog.init()),
                        (results, testResult) ->  new TestResults(
                                results.succeeded && testResult.succeeded,
                                results.testLog.add(testResult.testLog)),
                        (a, b) -> new TestResults(a.succeeded && b.succeeded, a.testLog.addAll(b.testLog)));
        System.out.println(result.testLog.createLogString());
        return result;
    }

    private static TestResult runTest(Test test) {
        var result = safefRunTest(test.test);
        var colour = getColour(result);
        return new TestResult(result.holds, new TestLogEntry(test.description, colour));
    }

    private static AssertionResult safefRunTest(Supplier<Assertion> test) {
        try {
            return test.get().run();
        } catch (Exception e) {
            return AssertionResult.failed(e);
        } catch (AssertionError e) {
            // TODO treat this differently, this should be invalid.
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
