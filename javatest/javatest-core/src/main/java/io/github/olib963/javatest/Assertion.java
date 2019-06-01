package io.github.olib963.javatest;

import io.github.olib963.javatest.assertions.CompositeAssertion;

public interface Assertion {
    AssertionResult run();

    default Assertion and(Assertion that){
        return new CompositeAssertion(this, that, (a, b) -> a && b, "and");
    }

    default Assertion or(Assertion that){
        return new CompositeAssertion(this, that, (a, b) -> a || b, "or");
    }

    default Assertion xor(Assertion that){
        return new CompositeAssertion(this, that, (a, b) -> a ^ b, "xor");
    }

}
