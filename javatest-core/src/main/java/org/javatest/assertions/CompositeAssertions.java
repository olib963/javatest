package org.javatest.assertions;

import org.javatest.JavaTest;

import java.util.function.BinaryOperator;

public class CompositeAssertions {
    private CompositeAssertions() {
    }

    static Assertion and(Assertion left, Assertion right) {
        return new ComposedAssertion(left, right, (a, b) -> a && b, "and");
    }

    static Assertion or(Assertion left, Assertion right) {
        return new ComposedAssertion(left, right, (a, b) -> a || b, "or");
    }

    static Assertion xor(Assertion left, Assertion right) {
        return new ComposedAssertion(left, right, (a, b) -> a ^ b, "xor");
    }

    static class ComposedAssertion implements Assertion {
        private final Assertion left;
        private final Assertion right;
        private final BinaryOperator<Boolean> combiner;
        private final String type;

        public ComposedAssertion(Assertion left, Assertion right, BinaryOperator<Boolean> combiner, String type) {
            this.left = left;
            this.right = right;
            this.combiner = combiner;
            this.type = type;
        }

        @Override
        public AssertionResult run() {
            var leftResult = left.run();
            var rightResult = right.run();
            var description = "(" + JavaTest.getColour(leftResult).getCode() + leftResult.description +
                    JavaTest.Colour.WHITE.getCode() + ") " + type +" (" +
                    JavaTest.getColour(rightResult).getCode() + rightResult.description + JavaTest.Colour.WHITE.getCode() + ")";
            if (leftResult.pending || rightResult.pending) {
                return AssertionResult.pending(description);
            }
            return AssertionResult.of(combiner.apply(leftResult.holds, rightResult.holds), description);
        }
    }
}
