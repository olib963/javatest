package io.github.olib963.javatest.runners;

import io.github.olib963.javatest.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamRunner implements TestRunner {
    private final Stream<? extends Testable> tests;
    private final Collection<TestCompletionObserver> observers;

    public StreamRunner(Stream<? extends Testable> tests, Collection<TestCompletionObserver> observers) {
        this.tests = tests;
        this.observers = observers;
    }

    @Override
    public TestResults run() {
        return tests
                .map(this::runTestable)
                .reduce(TestResults.init(), TestResults::addResult, TestResults::combine);
    }

    private TestResult runTestable(Testable testable) {
        return testable.match(
                test -> {
                    var result = runTest(test);
                    observers.forEach(o -> o.onTestCompletion(result));
                    return result;
                },
                suite -> {
                    var result = runSuite(suite);
                    observers.forEach(o -> o.onTestCompletion(result));
                    return result;
                }
        );
    }

    private TestResult runSuite(Testable.TestSuite suite) {
        var results = suite.testables.map(t -> t.match(this::runTest, this::runSuite));
        return new TestResult.SuiteResult(suite.name, results.collect(Collectors.toList()));

    }

    private TestResult runTest(Testable.Test test) {
        var result = safeRunTest(test.test);
        return new TestResult.SingleTestResult(result, List.of(test.name + ':', "\t" + result.description));
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
