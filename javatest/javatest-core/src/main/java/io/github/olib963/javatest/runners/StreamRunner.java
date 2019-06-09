package io.github.olib963.javatest.runners;

import io.github.olib963.javatest.*;
import io.github.olib963.javatest.logging.LoggingObserver;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamRunner implements TestRunner {
    private final Stream<Testable> tests;
    private final Collection<TestCompletionObserver> observers;

    public StreamRunner(Stream<Testable> tests, Collection<TestCompletionObserver> observers) {
        this.tests = tests;
        this.observers = observers;
    }

    @Override
    public TestResults run() {
        var results = tests
                .map(this::runTestable)
                .reduce(TestResults.init(), TestResults::combine);
        observers.forEach(o -> o.onRunCompletion(results));
        return results;
    }

    private TestResults runTestable(Testable testable) {
        return testable.tests().map(test -> runTest(testable.suiteName(), test))
                .reduce(TestResults.init(), TestResults::addResult, TestResults::combine);
    }

    private TestResult runTest(Optional<String> suiteName, Test test) {
        var result = safeRunTest(test.test);
        var log = test.name + LoggingObserver.SEPARATOR + "\t" + result.description;
        var withSuite = suiteName.map(n -> n + ':' + log).orElse(log);
        var testResult = new TestResult(result, withSuite);
        observers.forEach(o -> o.onTestCompletion(testResult));
        return testResult;
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
