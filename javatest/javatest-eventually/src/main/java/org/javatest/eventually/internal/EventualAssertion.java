package org.javatest.eventually.internal;

import org.javatest.Assertion;
import org.javatest.AssertionResult;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EventualAssertion implements Assertion {
    // TODO can the supplier throw an exception?
    private final Supplier<Assertion> test;
    private final Duration duration;
    private final int attempts;

    // TODO move this somehow to the stream. Can we zip with index?
    private int currentAttempt = 1;

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
        var result = Stream.generate(test).limit(attempts - 1)
                .reduce(runTest(test.get()),
                        (previousResult, test) -> {
                            if (previousResult.holds) {
                                return previousResult;
                            }
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                                return AssertionResult.exception(e);
                            }
                            return runTest(test);
                        }, (r1, r2) -> r1.holds ? r1 : r2);
        if (!result.holds) {
            return AssertionResult.of(false, result.description + " (Failed after " + attempts + " attempts)");
        }
        return result;
    }

    private AssertionResult runTest(Assertion assertion) {
        var result = assertion.run();
        if (result.holds) {
            var suffix = " (Passed on attempt " + currentAttempt + " of " + attempts + ")";
            return AssertionResult.of(true, result.description + suffix);
        }
        currentAttempt++;
        return result;
    }
}
