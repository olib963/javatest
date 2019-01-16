package org.javatest;

import org.javatest.matchers.Matcher;

public interface Assertion {

    boolean holds();

    // TODO should not fail test suite, but should be logged as separate things.
    class PendingAssertion implements Assertion {
        @Override
        public boolean holds() {
            return true;
        }
    }

    class SimpleAssertion implements Assertion {
        private final boolean holds;

        public SimpleAssertion(boolean holds) {
            this.holds = holds;
        }

        @Override
        public boolean holds() {
            return holds;
        }
    }

    class MatcherAssertion<A> implements Assertion {
        private final A value;
        private final Matcher<A> matcher;
        public MatcherAssertion(A value, Matcher<A> matcher) {
            this.value = value;
            this.matcher = matcher;
        }
        @Override
        public boolean holds() {
            return matcher.matches(value);
        }
    }

    static <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    static Assertion pending() {
        return new PendingAssertion();
    }

}

