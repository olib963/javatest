package org.javatest.matchers;

import org.javatest.matchers.internal.PredicateMatcher;

import java.util.function.Predicate;

public interface Matcher<A> {
    MatchResult matches(A value);
    String describeExpected();
    static <T> Matcher<T> fromFunctions(Predicate<T> predicate, String expected) {
        return new PredicateMatcher<>(predicate, expected);
    }
}
