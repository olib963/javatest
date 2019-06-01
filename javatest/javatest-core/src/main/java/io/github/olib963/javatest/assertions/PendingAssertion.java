package io.github.olib963.javatest.assertions;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;
import io.github.olib963.javatest.javafire.Assertion;

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