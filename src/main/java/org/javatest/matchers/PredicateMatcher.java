package org.javatest.matchers;

import java.util.function.Predicate;

public class PredicateMatcher<A> implements Matcher<A> {
    private final Predicate<A> predicate;
    private final String expected;
    public PredicateMatcher(Predicate<A> predicate, String expected) {
        this.predicate = predicate;
        this.expected = expected;
    }

    @Override
    public MatchResult matches(A value) {
        return predicate.test(value) ? MatchResult.match(expected) : MatchResult.mismatch(expected);
    }
}