package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

public class StringMatchers {

    private StringMatchers() {}

    public static Matcher<String> startsWith(final String prefix) {
        return PredicateMatcher.of(s -> s.startsWith(prefix), "start with {" + prefix + "}");
    }

    public static Matcher<String> endsWith(final String suffix) {
        return PredicateMatcher.of(s -> s.endsWith(suffix), "end with {" + suffix + "}");
    }

    public static Matcher<String> containsString(final String subString) {
        return PredicateMatcher.of(s -> s.contains(subString), "contain {" + subString + "}");
    }

    public static Matcher<String> isEmptyString() {
        return PredicateMatcher.of(String::isEmpty, "be the empty string");
    }

    public static Matcher<String> isBlankString() {
        return PredicateMatcher.of(String::isBlank, "be a blank string");
    }

    public static Matcher<String> isEqualToIgnoringCase(String expected) {
        return PredicateMatcher.of(s -> s.equalsIgnoreCase(expected), "be equal to (ignoring case) {" + expected + "}");
    }

    public static Matcher<String> hasLength(int length) {
        return PredicateMatcher.of(s -> s.length() == length, "have length {" + length + "}", s -> "had length {" + s.length() + "}");
    }
}
