package org.javatest.runners;

import org.javatest.*;
import org.javatest.logging.Colour;
import org.javatest.TestResult;

import java.util.Collection;
import java.util.stream.Stream;

public class StreamRunner implements TestRunner {
    private static final String SEPARATOR = System.lineSeparator();
    private final Stream<Test> tests;
    private final Collection<TestCompletionObserver> observers;

    public StreamRunner(Stream<Test> tests, Collection<TestCompletionObserver> observers) {
        this.tests = tests;
        this.observers = observers;
    }

    @Override
    public TestResults run() {
        var results = tests
                .map(this::runTest)
                .reduce(TestResults.init(), TestResults::addResult, TestResults::combine);

        observers.forEach(o -> o.onRunCompletion(results));
        return results;
    }

    private TestResult runTest(Test test) {
        // TODO allow a test to add to the log. Ideally immutable :/ probably have to be some kind of builder per test case.
        var result = safeRunTest(test.test());
        var colour = Colour.forResult(result);
        var log = colour.getCode() + test.name() + Colour.resetCode() + SEPARATOR + "\t" + result.description;
        var testResult = new TestResult(result, log);
        observers.forEach(o -> o.onTestCompletion(testResult));
        return testResult;
    }

    private AssertionResult safeRunTest(CheckedSupplier<Assertion> test) {
        try {
            return test.get().run();
        } catch (AssertionError e) {
            return AssertionResult.assertionThrown(e);
        } catch (Throwable e) {
            return AssertionResult.exception(e);
        }
    }
}
