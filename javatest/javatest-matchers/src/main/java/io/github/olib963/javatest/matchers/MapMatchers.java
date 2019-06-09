package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.EntryMatcher;
import io.github.olib963.javatest.matchers.internal.MapSetMatcher;
import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

import java.util.Map;

public class MapMatchers {

    private MapMatchers() {}

    public static <K, V> Matcher<Map<K, V>> isEmptyMap() {
        return PredicateMatcher.of(Map::isEmpty, "be an empty map");
    }

    public static <K, V> Matcher<Map<K, V>> hasMapSize(int size) {
        return PredicateMatcher.of(m -> m.size() == size, "have size {" + size + "}", m -> "had size {" + m.size() + "}");
    }

    public static <K, V> Matcher<Map<K, V>> hasKey(K key) {
        return PredicateMatcher.of(m -> m.containsKey(key), "contain the key {" + key + "}");
    }

    public static <K, V> Matcher<Map<K, V>> hasKeyThat(Matcher<K> keyMatcher) {
        return new MapSetMatcher<>(Map::keySet, keyMatcher, "key");
    }

    public static <K, V> Matcher<Map<K, V>> hasValue(V value) {
        return PredicateMatcher.of(m -> m.containsValue(value), "contain the value {" + value + "}");
    }

    public static <K, V> Matcher<Map<K, V>> hasValueThat(Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::values, valueMatcher, "value");
    }

    public static <K, V> Matcher<Map<K, V>> hasEntry(K key, V value) {
        var entry = Map.entry(key, value);
        return PredicateMatcher.of(
                m -> m.entrySet().contains(entry),
                "contain an entry with key {" + key + "} and value {" + value + "}"
        );
    }

    public static <K, V> Matcher<Map<K, V>> hasEntryThat(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::entrySet, new EntryMatcher<>(keyMatcher, valueMatcher), "entry");
    }

}

