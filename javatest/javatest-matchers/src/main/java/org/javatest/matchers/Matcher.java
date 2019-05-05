package org.javatest.matchers;

import org.javatest.Assertion;
import org.javatest.matchers.internal.MatcherAssertion;
import org.javatest.matchers.internal.PredicateMatcher;

import java.util.function.Predicate;

public interface Matcher<A> {
    MatchResult matches(A value);
    String describeExpected();

    static <T> Matcher<T> fromFunctions(Predicate<T> predicate, String expected) {
        return new PredicateMatcher<>(predicate, expected);
    }

    // TODO allow additional String information to be passed as a 3rd parameter
    static <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    static <A> Matcher<A> isEqualTo(A expected) {
        return PredicateMatcher.isEqualTo(expected);
    }

    static <A, T> Matcher<A> hasType(Class<T> expectedClass) {
        return PredicateMatcher.hasType(expectedClass);
    }

    static <A> Matcher<A> isTheSameInstanceAs(A instance) {
        return PredicateMatcher.isTheSameInstanceAs(instance);
    }
}
