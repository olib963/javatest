package org.javatest.eventually.internal;

import org.javatest.Assertion;
import org.javatest.AssertionResult;
import org.javatest.CheckedSupplier;

import java.util.stream.Stream;

public class EventualAssertion implements Assertion {
    private final CheckedSupplier<Assertion> test;
    private final int attempts;
    private final long sleepTime;

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

        if (sleepTime <= 0) {
            return AssertionResult.of(false, "Millisecond sleep time given must be greater than 0. (Sleep time was " + sleepTime + ")");
        }

        var firstAttempt = runTest(test, 1);

        var remainingAttempts = Stream
                .iterate(new Attempt(test, 2), Attempt::increment)
                .limit(attempts - 1);

        var result = remainingAttempts
                .reduce(firstAttempt,
                        this::nextAttempt,
                        (r1, r2) -> r1.holds ? r1 : r2);
        if (!result.holds) {
            return AssertionResult.of(false, result.description + " (Failed after " + attempts + " attempts)");
        }
        return result;
    }

    private AssertionResult nextAttempt(AssertionResult previousResult, Attempt attempt) {
        if (previousResult.holds) {
            return previousResult;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            return AssertionResult.exception(e);
        }
        return runTest(attempt.test, attempt.attempt);
    }

    private AssertionResult runTest(CheckedSupplier<Assertion> test, int currentAttempt) {
        try {
            var result = test.get().run();
            if (result.holds) {
                var suffix = " (Passed on attempt " + currentAttempt + " of " + attempts + ")";
                return AssertionResult.of(true, result.description + suffix);
            }
            return result;
        } catch (Exception e) {
            return AssertionResult.exception(e);
        }
    }

    private class Attempt {
        final CheckedSupplier<Assertion> test;
        final int attempt;
        public Attempt(CheckedSupplier<Assertion> test, int attempt) {
            this.test = test;
            this.attempt = attempt;
        }
        public Attempt increment() {
            return new Attempt(test, attempt + 1);
        }
    }
}
