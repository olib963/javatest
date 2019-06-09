package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;
import io.github.olib963.javatest.matchers.CheckedRunnable;
import io.github.olib963.javatest.matchers.Matcher;

public class MatcherAssertion<A> implements Assertion {
    private final A value;
    private final Matcher<A> matcher;
    public MatcherAssertion(A value, Matcher<A> matcher) {
        this.value = value;
        this.matcher = matcher;
    }

    @Override
    public AssertionResult run() {
        var matchResult = matcher.matches(value);
        var expectedPrefix = "Expected {" + toString(value) + "} to " + matcher.describeExpected();
        var description = matchResult.mismatch.map(mismatch -> expectedPrefix + " but " + mismatch).orElse(expectedPrefix);
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