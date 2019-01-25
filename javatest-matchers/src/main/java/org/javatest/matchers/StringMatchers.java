package org.javatest.matchers;

public interface StringMatchers {

    default Matcher<String> startsWith(final String prefix) {
        return new PredicateMatcher<>(s -> s.startsWith(prefix), "start with {" + prefix + "}");
    }

    default Matcher<String> endsWith(final String suffix) {
        return new PredicateMatcher<>(s -> s.endsWith(suffix), "end with {" + suffix + "}");
    }

    default Matcher<String> containsString(final String subString) {
        return new PredicateMatcher<>(s -> s.contains(subString), "contain {" + subString + "}");
    }

    default Matcher<String> isEmptyString() {
        return new PredicateMatcher<>(String::isEmpty, "be the empty string");
    }

    default Matcher<String> isBlankString() {
        return new PredicateMatcher<>(String::isBlank, "be a blank string");
    }

    default Matcher<String> isEqualToIgnoringCase(String expected) {
        return new PredicateMatcher<>(s -> s.equalsIgnoreCase(expected), "be equal to (ignoring case) {" + expected + "}");
    }

    default Matcher<String> hasLength(int length) {
        return new PredicateMatcher<>(s -> s.length() == length, "have length {" + length + "}", s -> "had length {" + s.length() + "}");
    }
}
