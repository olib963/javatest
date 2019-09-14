package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

public class NegatedMatcher<A> implements Matcher<A> {
    private final Matcher<A> toNegate;
    public NegatedMatcher(Matcher<A> toNegate) {
        this.toNegate = toNegate;
    }

    @Override
    public MatchResult matches(A value) {
        // TODO some cases may require a mismatch description for the negated case. Should we build in negation as a core concept rather than a wrapper?
        return MatchResult.of(!toNegate.matches(value).matches);
    }

    @Override
    public String describeExpected() {
        return "not " + toNegate.describeExpected();
    }
}
