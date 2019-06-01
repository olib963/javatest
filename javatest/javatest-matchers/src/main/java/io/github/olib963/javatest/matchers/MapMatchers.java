package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.javafire.matchers.internal.EntryMatcher;
import io.github.olib963.javatest.javafire.matchers.internal.MapSetMatcher;
import io.github.olib963.javatest.matchers.internal.EntryMatcher;
import io.github.olib963.javatest.matchers.internal.MapSetMatcher;
import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

import java.util.Map;

public class MapMatchers {

    private MapMatchers() {}

    public static <K, V> Matcher<Map<K, V>> isEmptyMap() {
        return new PredicateMatcher<>(Map::isEmpty, "be an empty map");
    }

    public static <K, V> Matcher<Map<K, V>> hasMapSize(int size) {
        return new PredicateMatcher<>(m -> m.size() == size, "have size {" + size + "}", m -> "had size {" + m.size() + "}");
    }

    public static <K, V> Matcher<Map<K, V>> hasKey(K key) {
        return new PredicateMatcher<>(m -> m.containsKey(key), "contain the key {" + key + "}");
    }

    public static <K, V> Matcher<Map<K, V>> hasKeyThat(Matcher<K> keyMatcher) {
        return new MapSetMatcher<>(Map::keySet, keyMatcher, "key");
    }

    public static <K, V> Matcher<Map<K, V>> hasValue(V value) {
        return new PredicateMatcher<>(m -> m.containsValue(value), "contain the value {" + value + "}");
    }

    public static <K, V> Matcher<Map<K, V>> hasValueThat(Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::values, valueMatcher, "value");
    }

    public static <K, V> Matcher<Map<K, V>> hasEntry(K key, V value) {
        var entry = Map.entry(key, value);
        return new PredicateMatcher<>(
                m -> m.entrySet().contains(entry),
                "contain an entry with key {" + key + "} and value {" + value + "}"
        );
    }

    // TODO is this a good enough name?
    public static <K, V> Matcher<Map<K, V>> hasEntryThat(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        return new MapSetMatcher<>(Map::entrySet, new EntryMatcher<>(keyMatcher, valueMatcher), "entry");
    }

}

