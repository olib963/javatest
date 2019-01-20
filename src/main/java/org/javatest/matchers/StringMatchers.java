package org.javatest.matchers;

public interface StringMatchers {

    default Matcher<String> startsWith(final String prefix) {
        return new PredicateMatcher<>(s -> s.startsWith(prefix));
    }

    default Matcher<String> endsWith(final String suffix) {
        return new PredicateMatcher<>(s -> s.endsWith(suffix));
    }

    default Matcher<String> containsString(final String subString) {
        return null;
    }

    default Matcher<String> isEmptyString() {
        return null;
    }

    default Matcher<String> isEqualToIgnoringCase(String expected) {
        return null;
    }

    default Matcher<String> hasLength(int length) {
        return null;
    }
}
