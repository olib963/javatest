package org.javatest.assertions;

import java.util.Optional;

public class PendingAssertion implements Assertion {
    private final Optional<String> description;
    PendingAssertion(Optional<String> description) {
        this.description = description;
    }
    @Override
    public AssertionResult run() {
        return new AssertionResult(true,  description, true);
    }

}