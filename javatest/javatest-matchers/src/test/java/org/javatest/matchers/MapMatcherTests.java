package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestSuite;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;
import static org.javatest.matchers.Matcher.*;
import static org.javatest.matchers.MapMatchers.*;
import static org.javatest.matchers.ComparableMatchers.*;
import static org.javatest.matchers.StringMatchers.*;

public class MapMatcherTests {
    private static final List<String> tags = List.of("map-matchers");
    private static final Map<Integer, String> SIMPLE_MAP = Map.of(1, "hello");

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
                    test("Empty", () -> that(Map.of(), isEmptyMap()), tags),
                    test("Size", () -> that(SIMPLE_MAP, hasMapSize(1)), tags),
                    test("Key", () -> that(SIMPLE_MAP, hasKey(1)), tags),
                    test("Key That", () -> that(SIMPLE_MAP, hasKeyThat(isLessThan(2))), tags),
                    test("Value", () -> that(SIMPLE_MAP, hasValue("hello")), tags),
                    test("Value That", () -> that(SIMPLE_MAP, hasValueThat(startsWith("hell"))), tags),
                    test("Entry", () -> that(SIMPLE_MAP, hasEntry(1, "hello")), tags),
                    test("Entry That", () -> that(SIMPLE_MAP, hasEntryThat(isLessThan(2), startsWith("hell"))), tags)
            );
        }
    }

    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Empty (FAIL)", () -> that(SIMPLE_MAP, isEmptyMap()), tags),
                    test("Size (FAIL)", () -> that(SIMPLE_MAP, hasMapSize(2)), tags),
                    test("Key (FAIL)", () -> that(SIMPLE_MAP, hasKey(2)), tags),
                    test("Key That (FAIL)", () -> that(SIMPLE_MAP, hasKeyThat(isGreaterThan(2))), tags),
                    test("Value (FAIL)", () -> that(SIMPLE_MAP, hasValue("goodbye")), tags),
                    test("Value That (FAIL)", () -> that(SIMPLE_MAP, hasValueThat(endsWith("bye"))), tags),
                    test("Entry (FAIL - key)", () -> that(SIMPLE_MAP, hasEntry(2, "hello")), tags),
                    test("Entry (FAIL - value)", () -> that(SIMPLE_MAP, hasEntry(1, "goodbye")), tags),
                    test("Entry (FAIL - both)", () -> that(SIMPLE_MAP, hasEntry(2, "goodbye")), tags),
                    test("Entry That (FAIL - key)", () -> that(SIMPLE_MAP, hasEntryThat(isGreaterThan(2), startsWith("hell"))), tags),
                    test("Entry That (FAIL - value)", () -> that(SIMPLE_MAP, hasEntryThat(isLessThan(2), endsWith("bye"))), tags),
                    test("Entry That (FAIL - both)", () -> that(SIMPLE_MAP, hasEntryThat(isGreaterThan(2), endsWith("bye"))), tags)
            );
        }
    }

}