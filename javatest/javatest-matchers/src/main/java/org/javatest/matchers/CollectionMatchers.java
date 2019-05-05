package org.javatest.matchers;

import org.javatest.matchers.internal.ElementThatMatcher;
import org.javatest.matchers.internal.PredicateMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CollectionMatchers {
    private CollectionMatchers() {}

    public static <T> Matcher<Collection<T>> hasSize(final int size) {
        return new PredicateMatcher<>(c -> c.size() == size, "have size {" + size + "}", c -> "had size {" + c.size() + "}");
    }

    public static <T> Matcher<Collection<T>> isEmpty() {
        return new PredicateMatcher<>(Collection::isEmpty, "be empty");
    }

    public static <T> Matcher<Collection<T>> contains(final T element) {
        return new PredicateMatcher<>(c -> c.contains(element), "contain {" + element + "}");
    }

    @SafeVarargs
    public static <T> Matcher<Collection<T>> containsAll(final T first, T... remaining) {
        // TODO it would be good to collect missing and not missing elements
        // TODO better way to construct this list???
        var elements = new ArrayList<>(remaining.length + 1);
        elements.add(first);
        elements.addAll(Arrays.asList(remaining));
        return new PredicateMatcher<>(c -> c.containsAll(elements), "contain all of {" + elements + "}");
    }

    public static <T> Matcher<Collection<T>> containsElementThat(Matcher<T> matcher) {
        return new ElementThatMatcher<>(matcher);
    }
}

