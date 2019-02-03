package org.javatest.assertions;

import java.util.Optional;

public class BooleanAssertion implements Assertion {
    private final boolean holds;
    private final Optional<String> description;

    // TODO enforce the setting of a description
    BooleanAssertion(boolean holds, Optional<String> description) {
        this.holds = holds;
        this.description = description;
    }

    @Override
    public AssertionResult run() {
        return new AssertionResult(holds, description, false);
    }
}