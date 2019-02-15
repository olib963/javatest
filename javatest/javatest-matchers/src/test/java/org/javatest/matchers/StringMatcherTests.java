package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.List;
import java.util.stream.Stream;

public class StringMatcherTests {
    private static final List<String> tags = List.of("string-matchers");
    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements MatcherTestProvider, StringMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("String Prefix", () -> that("Hello World", startsWith("Hello")), tags),
                    test("String Suffix", () -> that("Hello World", endsWith("World")), tags),
                    test("String Contains", () -> that("Hello World", containsString("llo")), tags),
                    test("String is empty", () -> that("", isEmptyString()), tags),
                    test("String is blank", () -> that("\t\n", isBlankString()), tags),
                    test("String is equal ignoring case", () -> that("HelLO WoRld", isEqualToIgnoringCase("hELlo wOrLD")), tags),
                    test("String has length", () -> that("Hello World", hasLength(11)), tags)
            );
        }
    }

    static class FailingTests implements MatcherTestProvider, StringMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("String Prefix (FAIL)", () -> that("Hello World", startsWith("World")), tags),
                    test("String Suffix (FAIL)", () -> that("Hello World", endsWith("Hello")), tags),
                    test("String Contains (FAIL)", () -> that("Hello World", containsString("Goodbye")), tags),
                    test("String is empty (FAIL)", () -> that("n", isEmptyString()), tags),
                    test("String is blank (FAIL)", () -> that("\t\n12", isBlankString()), tags),
                    test("String is equal ignoring case (FAIL)", () -> that("HelLO WoRld", isEqualToIgnoringCase("hELlo universe")), tags),
                    test("String has length (FAIL)", () -> that("Hello World", hasLength(10)), tags)
            );
        }
    }
}
