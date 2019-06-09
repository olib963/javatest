package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

import java.util.Collection;

public class ElementThatMatcher<T> implements Matcher<Collection<T>> {

    private final Matcher<T> elementMatcher;

    public ElementThatMatcher(Matcher<T> elementMatcher) {
        this.elementMatcher = elementMatcher;
    }

    @Override
    public MatchResult matches(Collection<T> value) {
        return MatchResult.of(
                value.stream()
                        .map(elementMatcher::matches)
                        .anyMatch(r -> r.matches));
    }

    @Override
    public String describeExpected() {
        return "contain an element that was expected to " + elementMatcher.describeExpected();
    }
}
