package org.javatest.assertions;

public class PendingAssertion implements Assertion {
    private final String description;
    PendingAssertion(String description) {
        this.description = description;
    }
    @Override
    public AssertionResult run() {
        return AssertionResult.pending(description);
    }

}