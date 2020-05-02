package io.github.olib963.javatest.runners;

import io.github.olib963.javatest.*;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamRunner implements TestRunner {
    private final Stream<? extends Testable> tests;

    public StreamRunner(Stream<? extends Testable> tests) {
        this.tests = tests;
    }

    @Override
    public TestResults run() {
        return tests
                .map(this::runTestable)
                .reduce(TestResults.empty(), TestResults::addResult, TestResults::combine);
    }

    private TestResult runTestable(Testable testable) {
        return testable.match(
                suite -> {
                    var result = runSuite(suite);
                    // TODO observers from config
//                    observers.forEach(o -> o.onTestCompletion(result));
                    return result;
                },
                test -> {
                    var result = runTest(test);
//                    observers.forEach(o -> o.onTestCompletion(result));
                    return result;
                }
        );
    }

    private TestResult runSuite(Testable.TestSuite suite) {
        var results = suite.testables().map(t -> t.match(this::runSuite, this::runTest));
        return new TestResult.SuiteResult(suite.name, results.collect(Collectors.toList()));

    }

    private TestResult runTest(Testable.Test test) {
        var result = safeRunTest(test.test);
        // A future feature will allow custom test logging client side.
        return new TestResult.SingleTestResult(test.name, result, Collections.emptyList());
    }

    private AssertionResult safeRunTest(CheckedSupplier<Assertion> test) {
        try {
            return test.get().run();
        } catch (AssertionError e) {
            return AssertionResult.assertionThrown(e);
        } catch (Exception e) {
            return AssertionResult.exception(e);
        }
    }
}
