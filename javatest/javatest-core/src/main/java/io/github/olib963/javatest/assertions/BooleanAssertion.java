package io.github.olib963.javatest.assertions;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;
import io.github.olib963.javatest.javafire.Assertion;

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