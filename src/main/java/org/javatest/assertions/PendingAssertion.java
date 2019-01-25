package org.javatest.assertions;

import java.util.Optional;

public class PendingAssertion implements Assertion {// TODO allow descriptions.
    PendingAssertion() {}
    @Override
    public AssertionResult run() {
        return new AssertionResult(true,  Optional.empty(), true);
    }

}