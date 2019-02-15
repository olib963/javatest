package org.javatest.matchers;

import org.javatest.Assertion;
import org.javatest.TestProvider;
import org.javatest.matchers.internal.MatcherAssertion;
import org.javatest.matchers.internal.PredicateMatcher;

public interface MatcherTestProvider extends TestProvider {
    // TODO allow additional String information to be passed as a 3rd parameter
    default <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    default <A> Matcher<A> isEqualTo(A expected) {
        return PredicateMatcher.isEqualTo(expected);
    }

    default <A, T> Matcher<A> hasType(Class<T> expectedClass) {
        return PredicateMatcher.hasType(expectedClass);
    }

    default <A> Matcher<A> isTheSameInstanceAs(A instance) {
        return PredicateMatcher.isTheSameInstanceAs(instance);
    }
}
