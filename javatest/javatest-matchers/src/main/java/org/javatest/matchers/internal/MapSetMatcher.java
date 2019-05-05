package org.javatest.matchers.internal;

import org.javatest.matchers.MatchResult;
import org.javatest.matchers.Matcher;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

// TODO another candidate for extraction
public class MapSetMatcher<K, V, E> implements Matcher<Map<K, V>> {

    private final Function<Map<K, V>, Collection<E>> setFunction;
    private final Matcher<E> elementMatcher;
    private final String type;

    public MapSetMatcher(Function<Map<K, V>, Collection<E>> setFunction, Matcher<E> elementMatcher, String type) {
        this.setFunction = setFunction;
        this.elementMatcher = elementMatcher;
        this.type = type;
    }

    @Override
    public MatchResult matches(Map<K, V> value) {
        // TODO mismatch descriptions
        return MatchResult.of(setFunction.apply(value)
                .stream()
                .map(elementMatcher::matches)
                .anyMatch(r -> r.matches));
    }

    @Override
    public String describeExpected() {
        return "have a " + type + " that was expected to " + elementMatcher.describeExpected();
    }
}
