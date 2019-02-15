package org.javatest.assertions;

import org.javatest.Assertion;
import org.javatest.AssertionResult;

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