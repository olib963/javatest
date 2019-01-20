package org.javatest.assertions;

public class PendingAssertion implements Assertion {
    PendingAssertion() {}
    @Override
    public AssertionResult run() {
        return new AssertionResult(true);
    }

}