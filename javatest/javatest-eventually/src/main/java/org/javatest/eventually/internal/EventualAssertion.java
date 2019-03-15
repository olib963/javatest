package org.javatest.eventually.internal;

import org.javatest.Assertion;
import org.javatest.AssertionResult;

import java.time.Duration;
import java.util.function.Supplier;

public class EventualAssertion implements Assertion {
    private final Supplier<Assertion> test;
    private final Duration duration;
    private final int attempts;

    public EventualAssertion(Supplier<Assertion> test, Duration duration, int attempts) {
        this.test = test;
        this.duration = duration;
        this.attempts = attempts;
    }

    @Override
    public AssertionResult run() {
        if (attempts < 1) {
            return AssertionResult.of(false, "You must make at least one attempt in your test. Attempts given: " + attempts);
        }
        var sleepTime = duration.abs().toMillis();
        AssertionResult result = null;
        // TODO how to fold in java instead of set null
        for (int attempt = 1; attempt <= attempts; attempt++) {
            // TODO can the supplier throw an exception?
            result = test.get().run();
            if (result.holds) {
                var suffix = " (Passed on attempt " + attempt + " of " + attempts + ")";
                return AssertionResult.of(true, result.description + suffix);
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                return AssertionResult.exception(e);
            }
        }
        if (result.holds) {
            return result;
        }
        return AssertionResult.of(false, result.description + " (Failed after " + attempts + " attempts)");
    }
}
