package io.github.olib963.javatest.runners;

import io.github.olib963.javatest.*;

import java.util.Collection;
import java.util.stream.Stream;

import static io.github.olib963.javatest.logging.RunLoggingObserver.SEPARATOR;

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
                .reduce(TestResults.init(), TestResults::combine);
    }

    private TestResults runTestable(Testable testable) {
        return testable.match(this::runTest, this::runSuite);
    }

    // TODO suite results as separate type. We want ${suite name}:all test results
    private TestResults runSuite(Testable.TestSuite suite) {
        return suite.testables.map(this::runTestable)
                .reduce(TestResults.init(), TestResults::combine);
    }

    private TestResults runTest(Testable.Test test) {
        var result = safeRunTest(test.test);
        var log = test.name + SEPARATOR + "\t" + result.description;
        var testResult = new TestResult(result, log);
        observers.forEach(o -> o.onTestCompletion(testResult));
        return TestResults.init().addResult(testResult);
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
