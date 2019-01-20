package org.javatest.assertions;

import org.javatest.matchers.Matcher;

public class MatcherAssertion<A> implements Assertion {
    private final A value;
    private final Matcher<A> matcher;
    MatcherAssertion(A value, Matcher<A> matcher) {
        this.value = value;
        this.matcher = matcher;
    }
    @Override
    public AssertionResult run() {
        return new AssertionResult(matcher.matches(value));
    }
}