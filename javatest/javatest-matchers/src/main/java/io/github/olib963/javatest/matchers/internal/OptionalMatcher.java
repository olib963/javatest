package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

import java.util.Optional;

public class OptionalMatcher<T> implements Matcher<Optional<T>> {
    private final Matcher<T> innerMatcher;
    public OptionalMatcher(Matcher<T> innerMatcher) {
        this.innerMatcher = innerMatcher;
    }

    @Override
    public MatchResult matches(Optional<T> value) {
        return value.map(innerMatcher::matches)
                .map(result -> result.mapMismatch(error -> "optional contained " + error))
                .orElseGet(() -> MatchResult.mismatch("Optional was empty"));
    }

    @Override
    public String describeExpected() {
        return "be an optional containing a value that was expected to " + innerMatcher.describeExpected();
    }
}
