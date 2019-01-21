package org.javatest.assertions;

import java.util.Collections;

public class PendingAssertion implements Assertion {
    PendingAssertion() {}
    @Override
    public AssertionResult run() {
        return new AssertionResult(true,  Collections.emptyList(), true);
    }

}