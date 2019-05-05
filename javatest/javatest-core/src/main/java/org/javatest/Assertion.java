package org.javatest;

import org.javatest.assertions.CompositeAssertion;

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

}
