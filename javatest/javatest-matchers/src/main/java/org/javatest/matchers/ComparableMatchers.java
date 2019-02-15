package org.javatest.matchers;

import org.javatest.matchers.internal.PredicateMatcher;

public interface ComparableMatchers {
    // TODO we might need to allow providing a Comparator<T>

    default <T extends Comparable<T>> Matcher<T> isComparablyEqualTo(T value) {
        // TODO should we describe mismatch as less/greater than
        return new PredicateMatcher<>(o -> o.compareTo(value) == 0, "be comparably equal to {" + value + "}");
    }
    default <T extends Comparable<T>> Matcher<T> isLessThan(T value) {
        return new PredicateMatcher<>(o -> o.compareTo(value) < 0, "be less than {" + value + "}");
    }
    default <T extends Comparable<T>> Matcher<T> isLessThanOrEqualTo(T value) {
        return new PredicateMatcher<>(o -> o.compareTo(value) <= 0, "be less than or comparably equal to {" + value + "}");
    }
    default <T extends Comparable<T>> Matcher<T> isGreaterThan(T value) {
        return new PredicateMatcher<>(o -> o.compareTo(value) > 0, "be greater than {" + value + "}");
    }
    default <T extends Comparable<T>> Matcher<T> isGreaterThanOrEqualTo(T value) {
        return new PredicateMatcher<>(o -> o.compareTo(value) >= 0, "be greater than or comparably equal to {" + value + "}");
    }
}
