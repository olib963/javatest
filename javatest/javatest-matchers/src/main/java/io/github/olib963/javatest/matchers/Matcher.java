package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.matchers.internal.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Matcher<A> {
    MatchResult matches(A value);
    String describeExpected();

    default Matcher<A> and(Matcher<A> that) {
        return new CompositeMatcher<>(this, that);
    }

    static <A> Matcher<A> not(Matcher<A> matcher) {
        return new NegatedMatcher<>(matcher);
    }

    static <A> Matcher<A> fromFunctions(Predicate<A> predicate, String expected) {
        return PredicateMatcher.of(predicate, expected);
    }

    static <A> Matcher<A> fromFunctions(Predicate<A> predicate, String expected, Function<A, String> mismatchDescriptionFunction) {
        return PredicateMatcher.of(predicate, expected, mismatchDescriptionFunction);
    }

    static <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(Optional.empty(), value, matcher);
    }

    static <A> Assertion that(String messagePrefix, A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(Optional.of(messagePrefix), value, matcher);
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

    /**
     * Utility function to create a better formatted string for types such as arrays. By default uses .toString()
     */
    static String stringify(Object object) {
        return Stringify.stringify(object);
    }
}
