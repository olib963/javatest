package org.javatest.assertions;

public class PendingAssertion implements Assertion {
    PendingAssertion() {}
    @Override
    public boolean holds() {
        return true;
    }

}