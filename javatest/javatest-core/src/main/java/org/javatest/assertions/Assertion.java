package org.javatest.assertions;

public interface Assertion {

    AssertionResult run();

    default Assertion and(Assertion other){
       return CompositeAssertion.and(this, other);
    }

    default Assertion or(Assertion other){
        return CompositeAssertion.or(this, other);
    }

    default Assertion xor(Assertion other){
        return CompositeAssertion.xor(this, other);
    }

    static Assertion that(boolean asserted, String description) { return new BooleanAssertion(asserted, description); }

    static Assertion pending() {
        return pending("Test has not yet been written");
    }

    static Assertion pending(String reason) {
        return new PendingAssertion(reason);
    }

}
