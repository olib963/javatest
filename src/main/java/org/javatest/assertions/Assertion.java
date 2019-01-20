package org.javatest.assertions;

import org.javatest.matchers.Matcher;

public interface Assertion {

    AssertionResult run();

    default Assertion and(Assertion other){
       return that(this.run().holds && other.run().holds);
    }

    default Assertion or(Assertion other){
        return that(this.run().holds || other.run().holds);
    }

    default Assertion xor(Assertion other){
        return that(this.run().holds ^ other.run().holds);
    }

    static <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    static Assertion that(boolean asserted) { return new SimpleAssertion(asserted); }

    static Assertion pending() {
        return new PendingAssertion();
    }

}

