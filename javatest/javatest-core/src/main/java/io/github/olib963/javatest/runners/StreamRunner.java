package io.github.olib963.javatest.runners;

import io.github.olib963.javatest.*;
import io.github.olib963.javatest.testable.Test;
import io.github.olib963.javatest.testable.TestSuite;

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
        if(testable instanceof Test) {
            return TestResults.init().addResult(runTest((Test) testable));
        } else if(testable instanceof TestSuite) {
            return runSuite((TestSuite) testable);
        } else if(testable instanceof TestSuiteClass) {
            return runSuite(((TestSuiteClass) testable).toSuite());
        }
        // TODO add failure
        return TestResults.init();
    }

    // TODO suite results as separate type. We want ${suite name}:all test results
    private TestResults runSuite(TestSuite suite) {
        return suite.testables.map(this::runTestable)
                .reduce(TestResults.init(), TestResults::combine);
    }

    private TestResult runTest(Test test) {
        var result = safeRunTest(test.test);
        var log = test.name + SEPARATOR + "\t" + result.description;
        var testResult = new TestResult(result, log);
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
