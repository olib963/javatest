package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;
import io.github.olib963.javatest.matchers.CheckedRunnable;
import io.github.olib963.javatest.matchers.Matcher;

import java.util.Optional;

public class MatcherAssertion<A> implements Assertion {
    private final Optional<String> messagePrefix;
    private final A value;
    private final Matcher<A> matcher;
    public MatcherAssertion(Optional<String> messagePrefix, A value, Matcher<A> matcher) {
        this.messagePrefix = messagePrefix;
        this.value = value;
        this.matcher = matcher;
    }

    @Override
    public AssertionResult run() {
        var matchResult = matcher.matches(value);
        var expectedMessage = "Expected {" + toString(value) + "} to " + matcher.describeExpected();
        var withPrefix = messagePrefix.map(p -> p + ": " + expectedMessage).orElse(expectedMessage);
        var description = matchResult.mismatch.map(mismatch -> withPrefix + " but " + mismatch).orElse(withPrefix);
        return AssertionResult.of(matchResult.matches, description);
    }

    private String toString(Object value) {
        if (value == null) {
            return "null";
        } else if(value instanceof CheckedRunnable) {
            return "runnable";
        }
        return value.toString();
    }
}