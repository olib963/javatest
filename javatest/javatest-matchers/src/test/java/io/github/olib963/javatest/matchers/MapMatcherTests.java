package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Map;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.ComparableMatchers.isGreaterThan;
import static io.github.olib963.javatest.matchers.ComparableMatchers.isLessThan;
import static io.github.olib963.javatest.matchers.MapMatchers.*;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.StringMatchers.endsWith;
import static io.github.olib963.javatest.matchers.StringMatchers.startsWith;

public class MapMatcherTests {
    private static final Map<Integer, String> SIMPLE_MAP = Map.of(1, "hello");

    public static TestSuiteClass passing() {
        return new PassingTests();
    }

    public static TestSuiteClass failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuiteClass {
        @Override
        public Stream<Testable> testables() {
            return Stream.of(
                    JavaTest.test("Empty", () -> that(Map.of(), isEmptyMap())),
                    test("Size", () -> that(SIMPLE_MAP, hasMapSize(1))),
                    test("Key", () -> that(SIMPLE_MAP, hasKey(1))),
                    test("Key That", () -> that(SIMPLE_MAP, hasKeyThat(isLessThan(2)))),
                    test("Value", () -> that(SIMPLE_MAP, hasValue("hello"))),
                    test("Value That", () -> that(SIMPLE_MAP, hasValueThat(startsWith("hell")))),
                    test("Entry", () -> that(SIMPLE_MAP, hasEntry(1, "hello"))),
                    test("Entry That", () -> that(SIMPLE_MAP, hasEntryThat(isLessThan(2), startsWith("hell"))))
            );
        }
    }

    static class FailingTests implements TestSuiteClass {
        @Override
        public Stream<Testable> testables() {
            return Stream.of(
                    test("Empty (FAIL)", () -> that(SIMPLE_MAP, isEmptyMap())),
                    test("Size (FAIL)", () -> that(SIMPLE_MAP, hasMapSize(2))),
                    test("Key (FAIL)", () -> that(SIMPLE_MAP, hasKey(2))),
                    test("Key That (FAIL)", () -> that(SIMPLE_MAP, hasKeyThat(isGreaterThan(2)))),
                    test("Value (FAIL)", () -> that(SIMPLE_MAP, hasValue("goodbye"))),
                    test("Value That (FAIL)", () -> that(SIMPLE_MAP, hasValueThat(endsWith("bye")))),
                    test("Entry (FAIL - key)", () -> that(SIMPLE_MAP, hasEntry(2, "hello"))),
                    test("Entry (FAIL - value)", () -> that(SIMPLE_MAP, hasEntry(1, "goodbye"))),
                    test("Entry (FAIL - both)", () -> that(SIMPLE_MAP, hasEntry(2, "goodbye"))),
                    test("Entry That (FAIL - key)", () -> that(SIMPLE_MAP, hasEntryThat(isGreaterThan(2), startsWith("hell")))),
                    test("Entry That (FAIL - value)", () -> that(SIMPLE_MAP, hasEntryThat(isLessThan(2), endsWith("bye")))),
                    test("Entry That (FAIL - both)", () -> that(SIMPLE_MAP, hasEntryThat(isGreaterThan(2), endsWith("bye"))))
            );
        }
    }

}