package org.javatest.assertions;

import org.javatest.matchers.Matcher;

public interface Assertion {

    boolean holds();

    default Assertion and(Assertion other){
       return that(this.holds() && other.holds());
    }

    default Assertion or(Assertion other){
        return that(this.holds() || other.holds());
    }

    default Assertion xor(Assertion other){
        return that(this.holds() ^ other.holds());
    }

    static <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    static Assertion that(boolean asserted) { return new SimpleAssertion(asserted); }

    static Assertion pending() {
        return new PendingAssertion();
    }

    static Assertion failed(Exception error) {
        return new FailedAssertion();
    }

    static Assertion failed(Error error) {
        return new FailedAssertion();
    }

}

