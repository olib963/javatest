package org.javatest.assertions;

public class FailedAssertion implements Assertion {
    @Override
    public AssertionResult run() {
        return new AssertionResult(false);
    }
}
