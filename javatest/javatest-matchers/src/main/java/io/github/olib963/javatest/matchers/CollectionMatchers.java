package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.ElementThatMatcher;
import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CollectionMatchers {
    private CollectionMatchers() {}

    public static <T> Matcher<Collection<T>> hasSize(final int size) {
        return PredicateMatcher.of(c -> c.size() == size, "have size {" + size + "}", c -> "had size {" + c.size() + "}");
    }

    public static <T> Matcher<Collection<T>> isEmpty() {
        return PredicateMatcher.of(Collection::isEmpty, "be empty");
    }

    public static <T> Matcher<Collection<T>> contains(final T element) {
        return PredicateMatcher.of(c -> c.contains(element), "contain {" + Matcher.stringify(element) + "}");
    }

    @SafeVarargs
    public static <T> Matcher<Collection<T>> containsAll(final T first, T... remaining) {
        var elements = new ArrayList<>(remaining.length + 1);
        elements.add(first);
        elements.addAll(Arrays.asList(remaining));
        return PredicateMatcher.of(c -> c.containsAll(elements), "contain all of {" +  Matcher.stringify(elements) + "}");
    }

    public static <T> Matcher<Collection<T>> containsElementThat(Matcher<T> matcher) {
        return new ElementThatMatcher<>(matcher);
    }
}

