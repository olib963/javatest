package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

public class ComparableMatchers {
    private ComparableMatchers() {}

    public static <T extends Comparable<T>> Matcher<T> isComparablyEqualTo(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) == 0, "be comparably equal to {" + value + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isLessThan(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) < 0, "be less than {" + value + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isLessThanOrEqualTo(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) <= 0, "be less than or comparably equal to {" + value + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isGreaterThan(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) > 0, "be greater than {" + value + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isGreaterThanOrEqualTo(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) >= 0, "be greater than or comparably equal to {" + value + "}");
    }
}
