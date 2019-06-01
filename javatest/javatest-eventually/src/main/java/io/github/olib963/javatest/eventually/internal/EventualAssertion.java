package io.github.olib963.javatest.eventually.internal;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;
import io.github.olib963.javatest.CheckedSupplier;

import java.util.Optional;
import java.util.stream.Stream;

public class EventualAssertion implements Assertion {
    private final CheckedSupplier<Assertion> test;
    private final int attempts;
    private final long sleepTime;
    private final Optional<Long> initialDelay;

    public EventualAssertion(CheckedSupplier<Assertion> test, long sleepTime, int attempts, Optional<Long> initialDelay) {
        this.test = test;
        this.attempts = attempts;
        this.sleepTime = sleepTime;
        this.initialDelay = initialDelay;
    }

    @Override
    public AssertionResult run() {
        if (attempts < 1) {
            return AssertionResult.failure("You must make at least one attempt in your test. Attempts given: " + attempts);
        }
        if (sleepTime <= 0) {
            return AssertionResult.failure("Millisecond sleep time given must be greater than 0. (Sleep time was " + sleepTime + ")");
        }
        return initialDelay.flatMap(this::waitForInitialDelay).orElseGet(this::runAllAttempts);
    }

    private Optional<AssertionResult> waitForInitialDelay(Long delay) {
            if (delay <= 0) {
                return Optional.of(AssertionResult.failure("Millisecond initial delay time given must be greater than 0. (Delay time was " + delay + ")"));
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return Optional.of(AssertionResult.exception(e));
            }
            return Optional.empty();
    }

    private AssertionResult runAllAttempts() {
        var firstAttempt = runTest(test, 1);

        // TODO this is just zipWithIndex. Is there some Java equivalent?
        var remainingAttempts = Stream
                .iterate(new Attempt(test, 2), Attempt::increment)
                .limit(attempts - 1);

        var result = remainingAttempts
                .reduce(firstAttempt,
                        this::nextAttempt,
                        (r1, r2) -> r1.holds ? r1 : r2);
        if (!result.holds) {
            return AssertionResult.failure(result.description + " (Failed after " + attempts + " attempts)");
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
                return AssertionResult.success(result.description + suffix);
            }
            return result;
        } catch (Exception e) {
            return AssertionResult.exception(e);
        }
    }

    private class Attempt {
        final CheckedSupplier<Assertion> test;
        final int attempt;

        private Attempt(CheckedSupplier<Assertion> test, int attempt) {
            this.test = test;
            this.attempt = attempt;
        }

        private Attempt increment() {
            return new Attempt(test, attempt + 1);
        }
    }
}
