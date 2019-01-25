package org.javatest.matchers;

import org.javatest.assertions.Assertion;
import org.javatest.assertions.AssertionResult;

public class MatcherAssertion<A> implements Assertion {
    private final A value;
    private final Matcher<A> matcher;
    MatcherAssertion(A value, Matcher<A> matcher) {
        this.value = value;
        this.matcher = matcher;
    }

    @Override
    public AssertionResult run() {
        var matchResult = matcher.matches(value);
        var expectedPrefix = "Expected {" + toString(value) + "} to " + matcher.describeExpected();
        var description = matchResult.mismatch.map(mismatch -> expectedPrefix + " but " + mismatch).orElse(expectedPrefix);
        return new AssertionResult(matchResult.matches, description);
    }

    private String toString(Object value) {
        if (value == null) {
            return "null";
        } else if(value instanceof Runnable) {
            return "runnable";
        }
        return value.toString();
    }
}