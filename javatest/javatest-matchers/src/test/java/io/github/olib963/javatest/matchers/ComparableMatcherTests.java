package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
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
                        test("Greater than or equal to (greater)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(9)))),
                        // Custom Comparators
                        test("Comparably Equal (Custom Comparator)", () -> that(point(1, 10), isComparablyEqualToUsing(point(1, 20), COMPARATOR))),
                        test("Less than (Custom Comparator)", () -> that(point(9, 10), isLessThanUsing(point(10, 10), COMPARATOR))),
                        test("Less than or equal to (equal) (Custom Comparator)", () -> that(point(10, 10), isLessThanOrEqualToUsing(point(10, 10), COMPARATOR))),
                        test("Less than or equal to (less) (Custom Comparator)", () -> that(point(10, 10), isLessThanOrEqualToUsing(point(11, 9), COMPARATOR))),
                        test("Greater than (Custom Comparator)", () -> that(point(10, 10), isGreaterThanUsing(point(9, 10), COMPARATOR))),
                        test("Greater than or equal to (equal) (Custom Comparator)", () -> that(point(10, 10), isGreaterThanOrEqualToUsing(point(10,10), COMPARATOR))),
                        test("Greater than or equal to (greater) (Custom Comparator)", () -> that(point(10, 10), isGreaterThanOrEqualToUsing(point(9,11), COMPARATOR)))
                ),
                Stream.of(
                        test("Comparably Equal (FAIL)", () -> that(wrap(10), isComparablyEqualTo(wrap(11)))),
                        test("Less than (FAIL - equal)", () -> that(wrap(10), isLessThan(wrap(10)))),
                        test("Less than (FAIL - greater)", () -> that(wrap(10), isLessThan(wrap(9)))),
                        test("Less than or equal to (FAIL)", () -> that(wrap(10), isLessThanOrEqualTo(wrap(9)))),
                        test("Greater than (FAIL - equal)", () -> that(wrap(10), isGreaterThan(wrap(10)))),
                        test("Greater than (FAIL - less)", () -> that(wrap(10), isGreaterThan(wrap(11)))),
                        test("Greater than or equal to (FAIL)", () -> that(wrap(10), isGreaterThanOrEqualTo(wrap(11)))),
                        // Custom Comparators
                        test("Comparably Equal (Custom Comparator) (FAIL)", () -> that(point(1, 10), isComparablyEqualToUsing(point(2, 10), COMPARATOR))),
                        test("Less than (Custom Comparator) (FAIL - equal)", () -> that(point(10, 10), isLessThanUsing(point(10, 10), COMPARATOR))),
                        test("Less than (Custom Comparator) (FAIL - greater)", () -> that(point(10, 10), isLessThanUsing(point(9, 9), COMPARATOR))),
                        test("Less than or equal to (Custom Comparator) (FAIL)", () -> that(point(10, 10), isLessThanOrEqualToUsing(point(9, 10), COMPARATOR))),
                        test("Greater than (Custom Comparator) (FAIL - equal)", () -> that(point(10, 10), isGreaterThanUsing(point(10, 10), COMPARATOR))),
                        test("Greater than (Custom Comparator) (FAIL - less)", () -> that(point(10, 10), isGreaterThanUsing(point(11, 11), COMPARATOR))),
                        test("Greater than or equal to (greater) (Custom Comparator) (FAIL)", () -> that(point(10, 10), isGreaterThanOrEqualToUsing(point(11,9), COMPARATOR)))
                )
        );
    }

    private static ComparableInt wrap(int value) {
        return new ComparableInt(value);
    }

    private static class ComparableInt implements Comparable<ComparableInt> {
        private final Integer value;

        private ComparableInt(Integer value) {
            this.value = value;
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
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "ComparableInt{" +
                    "value=" + value +
                    '}';
        }
    }

    private static Point point(int x, int y) {
        return new Point(x, y);
    }

    private static class Point {
        private final int x;
        private final int y;
        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        @Override
        public String toString() {
            return "(" + x + ',' + y +')';
        }
    }

    private static final XComparator COMPARATOR = new XComparator();

    private static class XComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return Integer.compare(o1.x, o2.x);
        }

        @Override
        public String toString() {
            return "X axis comparator";
        }
    }
}
