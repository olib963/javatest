package org.javatest.assertions;

// TODO abstraction
public class CompositeAssertions {
    private CompositeAssertions() {
    }

    static Assertion and(Assertion left, Assertion right) {
        return new AndAssertion(left, right);
    }

    static Assertion or(Assertion left, Assertion right) {
        return new OrAssertion(left, right);
    }

    static Assertion xor(Assertion left, Assertion right) {
        return new XorAssertion(left, right);
    }

    static class AndAssertion implements Assertion {
        private final Assertion left;
        private final Assertion right;

        private AndAssertion(Assertion left, Assertion right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public AssertionResult run() {
            var leftResult = left.run();
            var rightResult = right.run();
            // TODO denote which ones failed. Use colours?
            var description = "(" + leftResult.description + ") and (" + rightResult.description + ")";
            if (leftResult.pending || rightResult.pending) {
                return AssertionResult.pending(description);
            }
            return AssertionResult.of(leftResult.holds && rightResult.holds, description);
        }
    }

    static class OrAssertion implements Assertion {
        private final Assertion left;
        private final Assertion right;

        private OrAssertion(Assertion left, Assertion right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public AssertionResult run() {
            var leftResult = left.run();
            var rightResult = right.run();
            // TODO denote which ones failed. Use colours?
            var description = "(" + leftResult.description + ") or (" + rightResult.description + ")";
            if (leftResult.pending || rightResult.pending) {
                return AssertionResult.pending(description);
            }
            return AssertionResult.of(leftResult.holds || rightResult.holds, description);
        }
    }

    static class XorAssertion implements Assertion {
        private final Assertion left;
        private final Assertion right;

        private XorAssertion(Assertion left, Assertion right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public AssertionResult run() {
            var leftResult = left.run();
            var rightResult = right.run();
            // TODO denote which ones failed. Use colours?
            var description = "(" + leftResult.description + ") xor (" + rightResult.description + ")";
            if (leftResult.pending || rightResult.pending) {
                return AssertionResult.pending(description);
            }
            return AssertionResult.of(leftResult.holds ^ rightResult.holds, description);
        }
    }
}
