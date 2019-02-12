package org.javatest.assertions;

public class BooleanAssertion implements Assertion {
    private final boolean holds;
    private final String description;

    public BooleanAssertion(boolean holds, String description) {
        this.holds = holds;
        this.description = description;
    }

    @Override
    public AssertionResult run() {
        return AssertionResult.of(holds, description);
    }
}