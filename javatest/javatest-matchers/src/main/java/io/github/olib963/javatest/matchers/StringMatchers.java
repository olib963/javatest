package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

public class StringMatchers {

    private StringMatchers() {}

    public static Matcher<String> startsWith(final String prefix) {
        return new PredicateMatcher<>(s -> s.startsWith(prefix), "start with {" + prefix + "}");
    }

    public static Matcher<String> endsWith(final String suffix) {
        return new PredicateMatcher<>(s -> s.endsWith(suffix), "end with {" + suffix + "}");
    }

    public static Matcher<String> containsString(final String subString) {
        return new PredicateMatcher<>(s -> s.contains(subString), "contain {" + subString + "}");
    }

    public static Matcher<String> isEmptyString() {
        return new PredicateMatcher<>(String::isEmpty, "be the empty string");
    }

    public static Matcher<String> isBlankString() {
        return new PredicateMatcher<>(String::isBlank, "be a blank string");
    }

    public static Matcher<String> isEqualToIgnoringCase(String expected) {
        return new PredicateMatcher<>(s -> s.equalsIgnoreCase(expected), "be equal to (ignoring case) {" + expected + "}");
    }

    public static Matcher<String> hasLength(int length) {
        return new PredicateMatcher<>(s -> s.length() == length, "have length {" + length + "}", s -> "had length {" + s.length() + "}");
    }
}
