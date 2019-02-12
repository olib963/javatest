package org.javatest.matchers;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface MapMatchers {

    default <K, V> Matcher<Map<K, V>> isEmptyMap() {
        return new PredicateMatcher<>(Map::isEmpty, "be an empty map");
    }

    default <K, V> Matcher<Map<K, V>> hasMapSize(int size) {
        return new PredicateMatcher<>(m -> m.size() == size, "have size {" + size + "}", m -> "had size {" + m.size() + "}");
    }

    default <K, V> Matcher<Map<K, V>> hasKey(K key) {
        return new PredicateMatcher<>(m -> m.containsKey(key), "contain the key {" + key + "}");
    }

    default <K, V> Matcher<Map<K, V>> hasKeyThat(Matcher<K> keyMatcher) {
        return new MapSetMatcher<>(Map::keySet, keyMatcher, "key");
    }

    default <K, V> Matcher<Map<K, V>> hasValue(V value) {
        return new PredicateMatcher<>(m -> m.containsValue(value), "contain the value {" + value + "}");
    }

    default <K, V> Matcher<Map<K, V>> hasValueThat(Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::values, valueMatcher, "value");
    }

    default <K, V> Matcher<Map<K, V>> hasEntry(K key, V value) {
        var entry = Map.entry(key, value);
        return new PredicateMatcher<>(
                m -> m.entrySet().contains(entry),
                "contain an entry with key {" + key + "} and value {" + value + "}"
        );
    }

    // TODO is this a good enough name?
    default <K, V> Matcher<Map<K, V>> hasEntryThat(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::entrySet, new EntryMatcher<>(keyMatcher, valueMatcher), "entry");
    }

}

// TODO another candidate for extraction
class MapSetMatcher<K, V, E> implements Matcher<Map<K, V>> {

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
        return setFunction.apply(value)
                .stream()
                .map(elementMatcher::matches)
                .anyMatch(r -> r.matches)?
                MatchResult.match() : MatchResult.mismatch();
    }

    @Override
    public String describeExpected() {
        return "have a " + type + " that was expected to " + elementMatcher.describeExpected();
    }
}

class EntryMatcher<K, V> implements Matcher<Map.Entry<K,V>> {
    private final Matcher<K> keyMatcher;
    private final Matcher<V> valueMatcher;

    public EntryMatcher(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    @Override
    public MatchResult matches(Map.Entry<K, V> value) {
        boolean matches = keyMatcher.matches(value.getKey()).matches && valueMatcher.matches(value.getValue()).matches;
        return matches ? MatchResult.match() : MatchResult.mismatch();
    }

    @Override
    public String describeExpected() {
        return "have key that was expected to " + keyMatcher.describeExpected()
                + " and value that was expected to " + valueMatcher.describeExpected();
    }
}