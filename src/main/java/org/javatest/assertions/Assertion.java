package org.javatest.assertions;

import org.javatest.matchers.Matcher;

public interface Assertion {

    boolean holds();

    static <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    static Assertion that(boolean asserted) { return new SimpleAssertion(asserted); }

    static Assertion pending() {
        return new PendingAssertion();
    }

}

