package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

import java.util.Map;
import java.util.Optional;

public class EntryMatcher<K, V> implements Matcher<Map.Entry<K, V>> {
    private final Matcher<K> keyMatcher;
    private final Matcher<V> valueMatcher;

    public EntryMatcher(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    @Override
    public MatchResult matches(Map.Entry<K, V> entry) {
        var key = entry.getKey();
        var keyResult = keyMatcher.matches(key);
        var keyMismatch = keyResult.mismatch.map(m -> "Key " + Matcher.stringify(key) + " " + m);

        var value = entry.getValue();
        var valueResult = valueMatcher.matches(value);
        var valueMismatch = valueResult.mismatch.map(m -> "value " + Matcher.stringify(value) + " " + m);
        boolean matches = keyResult.matches && valueResult.matches;
        return MatchResult.of(matches, combine(keyMismatch, valueMismatch));
    }

    @Override
    public String describeExpected() {
        return "have key that was expected to " + keyMatcher.describeExpected()
                + " and value that was expected to " + valueMatcher.describeExpected();
    }

    private Optional<String> combine(Optional<String> a, Optional<String> b) {
        if (a.isPresent()) {
            var aStr = a.get();
            String withB = b.map(bStr -> aStr + " " + bStr).orElse(aStr);
            return Optional.of(withB);
        }
        return b;
    }
}
