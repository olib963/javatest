package org.javatest.matchers;

public interface StringMatchers {

    default Matcher<String> startsWith(final String prefix) {
        return new PredicateMatcher<>(s -> s.startsWith(prefix));
    }

    default Matcher<String> endsWith(final String suffix) {
        return new PredicateMatcher<>(s -> s.endsWith(suffix));
    }

    default Matcher<String> containsString(final String subString) {
        return new PredicateMatcher<>(s -> s.contains(subString));
    }

    default Matcher<String> isEmptyString() {
        return new PredicateMatcher<>(String::isEmpty);
    }

    default Matcher<String> isBlankString() {
        return new PredicateMatcher<>(String::isBlank);
    }

    default Matcher<String> isEqualToIgnoringCase(String expected) {
        return new PredicateMatcher<>(s -> s.equalsIgnoreCase(expected));
    }

    default Matcher<String> hasLength(int length) {
        return new PredicateMatcher<>(s -> s.length() == length);
    }
}
