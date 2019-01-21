package org.javatest.assertions;

import org.javatest.matchers.Matcher;

import java.util.List;

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
        var expectedPrefix = "Expected {" + toString(value) + "} to "; // TODO is this the best way to create descriptions??
        return new AssertionResult(matchResult.matches, List.of(expectedPrefix + matchResult.expected));
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