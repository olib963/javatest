package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.StringMatchers.*;

public class StringMatcherTests {
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
                    JavaTest.test("String Prefix", () -> that("Hello World", startsWith("Hello"))),
                    test("String Suffix", () -> that("Hello World", endsWith("World"))),
                    test("String Contains", () -> that("Hello World", containsString("llo"))),
                    test("String is empty", () -> that("", isEmptyString())),
                    test("String is blank", () -> that("\t\n", isBlankString())),
                    test("String is equal ignoring case", () -> that("HelLO WoRld", isEqualToIgnoringCase("hELlo wOrLD"))),
                    test("String has length", () -> that("Hello World", hasLength(11)))
            );
        }
    }

    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("String Prefix (FAIL)", () -> that("Hello World", startsWith("World"))),
                    test("String Suffix (FAIL)", () -> that("Hello World", endsWith("Hello"))),
                    test("String Contains (FAIL)", () -> that("Hello World", containsString("Goodbye"))),
                    test("String is empty (FAIL)", () -> that("n", isEmptyString())),
                    test("String is blank (FAIL)", () -> that("\t\n12", isBlankString())),
                    test("String is equal ignoring case (FAIL)", () -> that("HelLO WoRld", isEqualToIgnoringCase("hELlo universe"))),
                    test("String has length (FAIL)", () -> that("Hello World", hasLength(10)))
            );
        }
    }
}
