package org.javatest.assertions;

import org.javatest.matchers.Matcher;

import java.util.Optional;

public interface Assertion {

    AssertionResult run();

    default Assertion and(Assertion other){
        // TODO split out description from other logs, need the assertions to have descriptions so that we can compose them.
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

    static Assertion that(boolean asserted) { return new SimpleAssertion(asserted, Optional.empty()); }

    static Assertion that(boolean asserted, String description) { return new SimpleAssertion(asserted, Optional.of(description)); }

    static Assertion pending() {
        return new PendingAssertion(Optional.empty());
    }

    static Assertion pending(String reason) {
        return new PendingAssertion(Optional.of(reason));
    }

}
