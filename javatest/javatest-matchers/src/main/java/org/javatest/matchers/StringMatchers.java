package org.javatest.matchers;

import org.javatest.matchers.internal.PredicateMatcher;

public class StringMatchers {

    private StringMatchers() {}

    static Matcher<String> startsWith(final String prefix) {
        return new PredicateMatcher<>(s -> s.startsWith(prefix), "start with {" + prefix + "}");
    }

    static Matcher<String> endsWith(final String suffix) {
        return new PredicateMatcher<>(s -> s.endsWith(suffix), "end with {" + suffix + "}");
    }

    static Matcher<String> containsString(final String subString) {
        return new PredicateMatcher<>(s -> s.contains(subString), "contain {" + subString + "}");
    }

    static Matcher<String> isEmptyString() {
        return new PredicateMatcher<>(String::isEmpty, "be the empty string");
    }

    static Matcher<String> isBlankString() {
        return new PredicateMatcher<>(String::isBlank, "be a blank string");
    }

    static Matcher<String> isEqualToIgnoringCase(String expected) {
        return new PredicateMatcher<>(s -> s.equalsIgnoreCase(expected), "be equal to (ignoring case) {" + expected + "}");
    }

    static Matcher<String> hasLength(int length) {
        return new PredicateMatcher<>(s -> s.length() == length, "have length {" + length + "}", s -> "had length {" + s.length() + "}");
    }
}
