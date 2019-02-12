package org.javatest.assertions;

public class PendingAssertion implements Assertion {
    private final String description;
    public PendingAssertion(String description) {
        this.description = description;
    }
    @Override
    public AssertionResult run() {
        return AssertionResult.pending(description);
    }

}