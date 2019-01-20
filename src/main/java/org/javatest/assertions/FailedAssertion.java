package org.javatest.assertions;

public class FailedAssertion implements Assertion {
    @Override
    public boolean holds() {
        return false;
    }
}
