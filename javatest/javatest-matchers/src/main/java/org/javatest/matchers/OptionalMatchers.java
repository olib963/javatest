package org.javatest.matchers;

import org.javatest.matchers.internal.OptionalMatcher;
import org.javatest.matchers.internal.PredicateMatcher;

import java.util.Optional;

public class OptionalMatchers {

    private OptionalMatchers() {}

    public static <T> Matcher<Optional<T>> isEmptyOptional() {
        return new PredicateMatcher<>(Optional::isEmpty, "be an empty Optional");
    }

    public static <T> Matcher<Optional<T>> isOptionalOf(T value) {
        return isOptionalThat(PredicateMatcher.isEqualTo(value));
    }

    public static <T> Matcher<Optional<T>> isOptionalThat(Matcher<T> matcher) {
        return new OptionalMatcher<>(matcher);
    }

}

