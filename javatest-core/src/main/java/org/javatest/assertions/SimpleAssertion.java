package org.javatest.assertions;

import java.util.Optional;

public class SimpleAssertion implements Assertion {
    private final boolean holds;
    private final Optional<String> description;

    SimpleAssertion(boolean holds, Optional<String> description) {
        this.holds = holds;
        this.description = description;
    }

    @Override
    public AssertionResult run() {
        return new AssertionResult(holds, description, false);
    }
}