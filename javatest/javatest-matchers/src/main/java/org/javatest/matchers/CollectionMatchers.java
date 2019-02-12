package org.javatest.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public interface CollectionMatchers {

    default <T> Matcher<Collection<T>> hasSize(final int size) {
        return new PredicateMatcher<>(c -> c.size() == size, "have size {" + size + "}", c -> "had size {" + c.size() + "}");
    }

    default <T> Matcher<Collection<T>> isEmpty() {
        return new PredicateMatcher<>(Collection::isEmpty, "be empty");
    }

    default <T> Matcher<Collection<T>> contains(final T element) {
        return new PredicateMatcher<>(c -> c.contains(element), "contain {" + element + "}");
    }

    default <T> Matcher<Collection<T>> containsAll(final T first, T... remaining) {
        // TODO would be good to collect missing and not missing elements
        // TODO better way to construct this list???
        var elements = new ArrayList<>(remaining.length +1);
        elements.add(first);
        elements.addAll(Arrays.asList(remaining));
        return new PredicateMatcher<>(c -> c.containsAll(elements), "contain all of {" + elements + "}");
    }

    default <T> Matcher<Collection<T>> containsElementThat(Matcher<T> matcher) {
        return new ElementThatMatcher<>(matcher);
    }
}

// TODO another candidate for extraction
class ElementThatMatcher<T> implements Matcher<Collection<T>> {

    private final Matcher<T> elementMatcher;

    public ElementThatMatcher(Matcher<T> elementMatcher) {
        this.elementMatcher = elementMatcher;
    }

    @Override
    public MatchResult matches(Collection<T> value) {
        // TODO do we need any mismatch descriptions for each element?
        return value.stream().map(elementMatcher::matches).anyMatch(r -> r.matches)?
                MatchResult.match(): MatchResult.mismatch();
    }

    @Override
    public String describeExpected() {
        return "contain an element that was expected to " + elementMatcher.describeExpected();
    }
}