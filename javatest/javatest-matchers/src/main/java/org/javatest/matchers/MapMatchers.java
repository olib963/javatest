package org.javatest.matchers;

import org.javatest.matchers.internal.EntryMatcher;
import org.javatest.matchers.internal.MapSetMatcher;
import org.javatest.matchers.internal.PredicateMatcher;

import java.util.Map;

public class MapMatchers {

    private MapMatchers() {}

    static <K, V> Matcher<Map<K, V>> isEmptyMap() {
        return new PredicateMatcher<>(Map::isEmpty, "be an empty map");
    }

    static <K, V> Matcher<Map<K, V>> hasMapSize(int size) {
        return new PredicateMatcher<>(m -> m.size() == size, "have size {" + size + "}", m -> "had size {" + m.size() + "}");
    }

    static <K, V> Matcher<Map<K, V>> hasKey(K key) {
        return new PredicateMatcher<>(m -> m.containsKey(key), "contain the key {" + key + "}");
    }

    static <K, V> Matcher<Map<K, V>> hasKeyThat(Matcher<K> keyMatcher) {
        return new MapSetMatcher<>(Map::keySet, keyMatcher, "key");
    }

    static <K, V> Matcher<Map<K, V>> hasValue(V value) {
        return new PredicateMatcher<>(m -> m.containsValue(value), "contain the value {" + value + "}");
    }

    static <K, V> Matcher<Map<K, V>> hasValueThat(Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::values, valueMatcher, "value");
    }

    static <K, V> Matcher<Map<K, V>> hasEntry(K key, V value) {
        var entry = Map.entry(key, value);
        return new PredicateMatcher<>(
                m -> m.entrySet().contains(entry),
                "contain an entry with key {" + key + "} and value {" + value + "}"
        );
    }

    // TODO is this a good enough name?
    static <K, V> Matcher<Map<K, V>> hasEntryThat(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::entrySet, new EntryMatcher<>(keyMatcher, valueMatcher), "entry");
    }

}

