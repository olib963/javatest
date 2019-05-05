package org.javatest.matchers.internal;

import org.javatest.matchers.MatchResult;
import org.javatest.matchers.Matcher;

import java.util.Collection;

// TODO another candidate for extraction
public class ElementThatMatcher<T> implements Matcher<Collection<T>> {

    private final Matcher<T> elementMatcher;

    public ElementThatMatcher(Matcher<T> elementMatcher) {
        this.elementMatcher = elementMatcher;
    }

    @Override
    public MatchResult matches(Collection<T> value) {
        // TODO do we need any mismatch descriptions for each element?
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
