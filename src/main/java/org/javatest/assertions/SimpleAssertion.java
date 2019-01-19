package org.javatest.assertions;

public class SimpleAssertion implements Assertion {
    private final boolean holds;

    SimpleAssertion(boolean holds) {
        this.holds = holds;
    }

    @Override
    public boolean holds() {
        return holds;
    }
}