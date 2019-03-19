package org.javatest.assertions;

import java.util.function.BinaryOperator;

import org.javatest.Assertion;
import org.javatest.AssertionResult;
import org.javatest.logging.Colour;

public class CompositeAssertion implements Assertion {
    private final Assertion left;
    private final Assertion right;
    private final BinaryOperator<Boolean> combiner;
    private final String type;

    public CompositeAssertion(Assertion left, Assertion right, BinaryOperator<Boolean> combiner, String type) {
        this.left = left;
        this.right = right;
        this.combiner = combiner;
        this.type = type;
    }

    @Override
    public AssertionResult run() {
        var leftResult = left.run();
        var rightResult = right.run();
        var description = "(" + Colour.forResult(leftResult).getCode() + leftResult.description +
                Colour.WHITE.getCode() + ") " + type +" (" +
                Colour.forResult( rightResult).getCode() + rightResult.description + Colour.WHITE.getCode() + ")";
        if (leftResult.pending || rightResult.pending) {
            return AssertionResult.pending(description);
        }
        return AssertionResult.of(combiner.apply(leftResult.holds, rightResult.holds), description);
    }

    public static Assertion and(Assertion left, Assertion right) {
        return new CompositeAssertion(left, right, (a, b) -> a && b, "and");
    }

    public static Assertion or(Assertion left, Assertion right) {
        return new CompositeAssertion(left, right, (a, b) -> a || b, "or");
    }

    public static Assertion xor(Assertion left, Assertion right) {
        return new CompositeAssertion(left, right, (a, b) -> a ^ b, "xor");
    }

}
