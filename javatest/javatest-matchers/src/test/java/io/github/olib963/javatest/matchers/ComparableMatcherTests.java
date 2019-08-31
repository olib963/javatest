package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.ComparableMatcherTests.ComparableInt.wrap;
import static io.github.olib963.javatest.matchers.ComparableMatchers.*;
import static io.github.olib963.javatest.matchers.Matcher.that;

public class ComparableMatcherTests {

    public static Testable.TestSuite suite() {
        return Utils.matcherSuite("Comparable Matcher Tests",
                Stream.of(
                        test("Comparably Equal", () -> that(wrap(10), isComparablyEqualTo(wrap(10)))),
                        test("Less than", () -> that(wrap(10), isLessThan(wrap(11)))),
                        test("Less than or equal to (equal)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(10)))),
                        test("Less than or equal to (less)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(11)))),
                        test("Greater than", () -> that(wrap(10), isGreaterThan(wrap(9)))),
                        test("Greater than or equal to (equal)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(10)))),
                        test("Greater than or equal to (greater)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(9))))
                ),
                Stream.of(
                        test("Comparably Equal (FAIL)", () -> that(wrap(10), isComparablyEqualTo(wrap(11)))),
                        test("Less than (FAIL - equal)", () -> that(wrap(10), isLessThan(wrap(10)))),
                        test("Less than (FAIL - greater)", () -> that(wrap(10), isLessThan(wrap(9)))),
                        test("Less than or equal to (FAIL)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(9)))),
                        test("Greater than (FAIL - equal)", () -> that(wrap(10), isGreaterThan(wrap(10)))),
                        test("Greater than (FAIL - less)", () -> that(wrap(10), isGreaterThan(wrap(11)))),
                        test("Greater than or equal to (FAIL)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(11))))
                )
        );
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
