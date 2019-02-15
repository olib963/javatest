package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.List;
import java.util.stream.Stream;

import static org.javatest.matchers.ComparableMatcherTests.ComparableInt.wrap;

public class ComparableMatcherTests {
    private static final List<String> tags = List.of("comparable-matchers");

    public static TestProvider passing() {
        return new PassingTests();
    }

    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements MatcherTestProvider, ComparableMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Comparably Equal", () -> that(wrap(10), isComparablyEqualTo(wrap(10))), tags),
                    test("Less than", () -> that(wrap(10), isLessThan(wrap(11))), tags),
                    test("Less than or equal to (equal)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(10))), tags),
                    test("Less than or equal to (less)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(11))), tags),
                    test("Greater than", () -> that(wrap(10), isGreaterThan(wrap(9))), tags),
                    test("Greater than or equal to (equal)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(10))), tags),
                    test("Greater than or equal to (greater)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(9))), tags)
            );
        }
    }

    static class FailingTests implements MatcherTestProvider, ComparableMatchers {
        @Override
        public Stream<Test> testStream() {
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
