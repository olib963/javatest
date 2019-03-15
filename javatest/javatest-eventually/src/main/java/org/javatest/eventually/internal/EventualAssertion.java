package org.javatest.eventually.internal;

import org.javatest.Assertion;
import org.javatest.AssertionResult;
import org.javatest.CheckedSupplier;

import java.util.stream.Stream;

public class EventualAssertion implements Assertion {
    private final CheckedSupplier<Assertion> test;
    private final int attempts;
    private final long sleepTime;

    // TODO move this somehow to the stream. Can we zip with index?
    private int currentAttempt = 1;

    public EventualAssertion(CheckedSupplier<Assertion> test, long sleepTime, int attempts) {
        this.test = test;
        this.attempts = attempts;
        this.sleepTime = sleepTime;
    }

    @Override
    public AssertionResult run() {
        if (attempts < 1) {
            return AssertionResult.of(false, "You must make at least one attempt in your test. Attempts given: " + attempts);
        }

        var result = Stream
                .generate(() -> test)
                .limit(attempts - 1)
                .reduce(runTest(test),
                        this::nextAttempt,
                        (r1, r2) -> r1.holds ? r1 : r2);
        if (!result.holds) {
            return AssertionResult.of(false, result.description + " (Failed after " + attempts + " attempts)");
        }
        return result;
    }

    private AssertionResult nextAttempt(AssertionResult previousResult, CheckedSupplier<Assertion> test) {
        if (previousResult.holds) {
            return previousResult;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            return AssertionResult.exception(e);
        }
        return runTest(test);
    }

    private AssertionResult runTest(CheckedSupplier<Assertion> test) {
        try {
            var result = test.get().run();
            if (result.holds) {
                var suffix = " (Passed on attempt " + currentAttempt + " of " + attempts + ")";
                return AssertionResult.of(true, result.description + suffix);
            }
            currentAttempt++;
            return result;
        } catch (Throwable e) {
            return AssertionResult.exception(e);
        }
    }
}
