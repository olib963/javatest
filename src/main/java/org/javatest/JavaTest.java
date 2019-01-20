package org.javatest;

import org.javatest.assertions.Assertion;
import org.javatest.assertions.PendingAssertion;
import org.javatest.logging.Colour;
import org.javatest.logging.TestLog;
import org.javatest.logging.TestLogEntry;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class JavaTest {
    public static TestResults run(Stream<Test> tests) {
        var result = tests
                .map(JavaTest::runTest)
                .reduce(new TestResults(true, TestLog.init()),
                (results, testResult) ->  new TestResults(
                        results.succeeded && testResult.succeeded,
                        results.testLog.add(testResult.testLog)),
                (a, b) -> new TestResults(a.succeeded && b.succeeded, a.testLog.addAll(b.testLog)));
        System.out.println(result.testLog.createLogString());
        return result;
    }

    private static TestResult runTest(Test test) {
        var assertion = safefRunSupplier(test.test);
        var holds = assertion.holds();
        var colour = getColour(assertion, holds);
        return new TestResult(holds, new TestLogEntry(test.description, colour));
    }

    private static Assertion safefRunSupplier(Supplier<Assertion> test) {
        try {
            return test.get();
        } catch (Exception e) {
            return Assertion.failed(e);
        } catch (AssertionError e) {
            return Assertion.failed(e);
        }
    }

    private static Colour getColour(Assertion assertion, boolean holds) {
        if (assertion instanceof PendingAssertion) {
            return Colour.YELLOW;
        } else if (holds) {
            return Colour.GREEN;
        } else {
            return Colour.RED;
        }
    }
}
