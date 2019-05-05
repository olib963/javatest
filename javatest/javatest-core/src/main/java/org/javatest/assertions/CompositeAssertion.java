package org.javatest.assertions;

import org.javatest.Assertion;
import org.javatest.AssertionResult;

import java.util.function.BinaryOperator;

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
        var description = "(" + leftResult.description + ' ' + holdString(leftResult) + ") " + type +
                " (" + rightResult.description + ' ' + holdString(rightResult) + ")";
        if (leftResult.pending || rightResult.pending) {
            return AssertionResult.pending(description);
        }
        return AssertionResult.of(combiner.apply(leftResult.holds, rightResult.holds), description);
    }

    private String holdString(AssertionResult result) {
        if(result.pending) {
            return "[pending]";
        }
        if(result.holds) {
            return "[holding]";
        }
        return "[not holding]";
    }

}
