package org.javatest.matchers;

import org.javatest.matchers.internal.OptionalMatcher;
import org.javatest.matchers.internal.PredicateMatcher;

import java.util.Optional;

public interface OptionalMatchers {

    default <T> Matcher<Optional<T>> isEmptyOptional() {
        return new PredicateMatcher<>(Optional::isEmpty, "be an empty Optional");
    }

    default <T> Matcher<Optional<T>> isOptionalOf(T value) {
        return isOptionalThat(PredicateMatcher.isEqualTo(value));
    }

    default <T> Matcher<Optional<T>> isOptionalThat(Matcher<T> matcher) {
        return new OptionalMatcher<>(matcher);
    }

}

