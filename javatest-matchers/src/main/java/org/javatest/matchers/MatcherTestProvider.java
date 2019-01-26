package org.javatest.matchers;

import org.javatest.assertions.Assertion;
import org.javatest.tests.TestProvider;

public interface MatcherTestProvider extends TestProvider {
    default <A> Assertion that(A value, Matcher<A> matcher) {
        return new MatcherAssertion<>(value, matcher);
    }

    default <A> Matcher<A> isEqualTo(A expected) {
        return Matcher.isEqualTo(expected);
    }

    default <A, T> Matcher<A> hasType(Class<T> expectedClass) {
        return Matcher.hasType(expectedClass);
    }
}
