package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.PredicateMatcher;

import java.util.Comparator;

public class ComparableMatchers {
    private ComparableMatchers() {}

    public static <T extends Comparable<T>> Matcher<T> isComparablyEqualTo(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) == 0, "be comparably equal to {" + Matcher.stringify(value) + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isLessThan(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) < 0, "be less than {" + Matcher.stringify(value) + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isLessThanOrEqualTo(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) <= 0, "be less than or comparably equal to {" + Matcher.stringify(value) + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isGreaterThan(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) > 0, "be greater than {" + Matcher.stringify(value) + "}");
    }

    public static <T extends Comparable<T>> Matcher<T> isGreaterThanOrEqualTo(T value) {
        return PredicateMatcher.of(o -> o.compareTo(value) >= 0, "be greater than or comparably equal to {" + Matcher.stringify(value) + "}");
    }

    public static <T> Matcher<T> isComparablyEqualToUsing(T value, Comparator<T> comparator) {
        return PredicateMatcher.of(o -> comparator.compare(o, value) == 0, "be comparably equal to {" + Matcher.stringify(value) + "} using {" + Matcher.stringify(comparator) + "}");
    }

    public static <T> Matcher<T> isLessThanUsing(T value, Comparator<T> comparator) {
        return PredicateMatcher.of(o -> comparator.compare(o, value) < 0, "be less than {" + Matcher.stringify(value) + "} using {" + Matcher.stringify(comparator) + "}");
    }

    public static <T> Matcher<T> isLessThanOrEqualToUsing(T value, Comparator<T> comparator) {
        return PredicateMatcher.of(o -> comparator.compare(o, value) <= 0, "be less than or comparably equal to {" + Matcher.stringify(value) + "} using {" + Matcher.stringify(comparator) + "}");
    }

    public static <T> Matcher<T> isGreaterThanUsing(T value, Comparator<T> comparator) {
        return PredicateMatcher.of(o -> comparator.compare(o, value) > 0, "be greater than {" + Matcher.stringify(value) + "} using {" + Matcher.stringify(comparator) + "}");
    }

    public static <T> Matcher<T> isGreaterThanOrEqualToUsing(T value, Comparator<T> comparator) {
        return PredicateMatcher.of(o -> comparator.compare(o, value) >= 0, "be greater than or comparably equal to {" + Matcher.stringify(value) + "} using {" + Matcher.stringify(comparator) + "}");
    }
}
