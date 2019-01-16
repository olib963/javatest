package org.javatest;

public interface Assertion {

    boolean holds();

    class SimpleAssertion implements Assertion {
        private final boolean holds;

        public SimpleAssertion(boolean holds) {
            this.holds = holds;
        }

        @Override
        public boolean holds() {
            return holds;
        }
    }

}

