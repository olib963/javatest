package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.ComparableMatcherTests.ComparableInt.wrap;
import static io.github.olib963.javatest.matchers.ComparableMatchers.*;
import static io.github.olib963.javatest.matchers.Matcher.that;

public class ComparableMatcherTests {
    private static final List<String> tags = List.of("comparable-matchers");

    public static TestSuite passing() {
        return new PassingTests();
    }

    public static TestSuite failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    JavaTest.test("Comparably Equal", () -> that(wrap(10), isComparablyEqualTo(wrap(10))), tags),
                    test("Less than", () -> that(wrap(10), isLessThan(wrap(11))), tags),
                    test("Less than or equal to (equal)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(10))), tags),
                    test("Less than or equal to (less)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(11))), tags),
                    test("Greater than", () -> that(wrap(10), isGreaterThan(wrap(9))), tags),
                    test("Greater than or equal to (equal)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(10))), tags),
                    test("Greater than or equal to (greater)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(9))), tags)
            );
        }
    }

    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Comparably Equal (FAIL)", () -> that(wrap(10), isComparablyEqualTo(wrap(11))), tags),
                    test("Less than (FAIL - equal)", () -> that(wrap(10), isLessThan(wrap(10))), tags),
                    test("Less than (FAIL - greater)", () -> that(wrap(10), isLessThan(wrap(9))), tags),
                    test("Less than or equal to (FAIL)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(9))), tags),
                    test("Greater than (FAIL - equal)", () -> that(wrap(10), isGreaterThan(wrap(10))), tags),
                    test("Greater than (FAIL - less)", () -> that(wrap(10), isGreaterThan(wrap(11))), tags),
                    test("Greater than or equal to (FAIL)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(11))), tags)
            );
        }
    }

    static class ComparableInt implements Comparable<ComparableInt> {
        private final Integer value;
        public ComparableInt(Integer value) {
            this.value = value;
        }
        public static ComparableInt wrap(int value) {
            return new ComparableInt(value);
        }

        @Override
        public int compareTo(ComparableInt o) {
            return this.value.compareTo(o.value);
        }

        @Override
        public boolean equals(Object o) {
            throw new RuntimeException("We don't want equality tests on this class, just comparable tests");
        }

        @Override
        public String toString() {
            return "ComparableInt{" +
                    "value=" + value +
                    '}';
        }
    }
}
