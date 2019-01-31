package org.javatest.matchers;

import java.util.Optional;

public interface OptionalMatchers {

    default <T> Matcher<Optional<T>> isEmptyOptional() {
        return new PredicateMatcher<>(Optional::isEmpty, "be an empty Optional");
    }

    default <T> Matcher<Optional<T>> isOptionalOf(T value) {
        return isOptionalThat(Matcher.isEqualTo(value));
    }

    default <T> Matcher<Optional<T>> isOptionalThat(Matcher<T> matcher) {
        return new OptionalMatcher<>(matcher);
    }

}

// TODO similar to cause and message matcher this is just a couple of wrapping functions pre and post result. Find one more concretion and find the abstraction.
class OptionalMatcher<T> implements Matcher<Optional<T>> {
    private final Matcher<T> innerMatcher;
    public OptionalMatcher(Matcher<T> innerMatcher) {
        this.innerMatcher = innerMatcher;
    }

    @Override
    public MatchResult matches(Optional<T> value) {
        return value.map(innerMatcher::matches)
                // TODO mismatch messages could use some work
                .map(result -> result.mapMismatch(error -> "optional contained " + error))
                .orElseGet(() -> MatchResult.mismatch("Optional was empty"));
    }

    @Override
    public String describeExpected() {
        return "be an optional containing a value that was expected to " + innerMatcher.describeExpected();
    }
}
