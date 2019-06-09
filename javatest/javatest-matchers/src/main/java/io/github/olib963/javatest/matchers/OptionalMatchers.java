package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.OptionalMatcher;
import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

import java.util.Optional;

public class OptionalMatchers {

    private OptionalMatchers() {}

    public static <T> Matcher<Optional<T>> isEmptyOptional() {
        return PredicateMatcher.of(Optional::isEmpty, "be an empty Optional");
    }

    public static <T> Matcher<Optional<T>> isOptionalOf(T value) {
        return isOptionalThat(PredicateMatcher.isEqualTo(value));
    }

    public static <T> Matcher<Optional<T>> isOptionalThat(Matcher<T> matcher) {
        return new OptionalMatcher<>(matcher);
    }

}

