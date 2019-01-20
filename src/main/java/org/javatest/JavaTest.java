package org.javatest;

import org.javatest.assertions.Assertion;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class JavaTest {
    public static TestResults run(Stream<Test> tests) {
        var result = tests
                .map(JavaTest::runTest)
                .reduce(new TestResults(true, ""),
                (results, testResult) ->  new TestResults(results.succeeded && testResult.succeeded, results.testLog + System.lineSeparator() + testResult.testLog),
                (a, b) -> new TestResults(a.succeeded && b.succeeded, a.testLog + System.lineSeparator() + b.testLog));
        System.out.println(result.testLog);
        return result;
    }

    private static TestResult runTest(Test test) {
        var assertion = safefRunSupplier(test.test);
        return new TestResult(assertion.holds(), test.description);
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
}
