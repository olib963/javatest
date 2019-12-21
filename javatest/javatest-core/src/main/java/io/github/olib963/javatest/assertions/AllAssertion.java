package io.github.olib963.javatest.assertions;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;

import java.util.Collection;
import java.util.stream.Collectors;

public class AllAssertion implements Assertion {
    private final Collection<Assertion> assertions;

    public AllAssertion(Collection<Assertion> assertions) {
        this.assertions = assertions;
    }

    @Override
    public AssertionResult run() {
        var results = assertions.stream().map(Assertion::run).collect(Collectors.toList());
        var description = results.stream()
                .map(r -> "(" + r.description + ' ' + CompositeAssertion.holdString(r) + ")")
                // TODO structured log with indentation consistency
                .collect(Collectors.joining(" and\n"));
        if (results.stream().anyMatch(r -> r.pending)) {
            return AssertionResult.pending(description);
        }
        return AssertionResult.of(results.stream().allMatch(r -> r.holds), description);
    }

}
