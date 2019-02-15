package org.javatest.matchers;

import org.javatest.matchers.internal.EntryMatcher;
import org.javatest.matchers.internal.MapSetMatcher;
import org.javatest.matchers.internal.PredicateMatcher;

import java.util.Map;

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

