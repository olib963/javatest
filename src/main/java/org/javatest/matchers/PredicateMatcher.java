package org.javatest.matchers;

import java.util.function.Predicate;

public class PredicateMatcher<A> implements Matcher<A> {
    private final Predicate<A> predicate;

    PredicateMatcher(Predicate<A> predicate) {
        this.predicate = predicate;
    }

    @Override
    public MatchResult matches(A value) {
        return predicate.test(value) ? MatchResult.match() : MatchResult.mismatch();
    }
}