package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.OptionalMatchers.*;
import static io.github.olib963.javatest.matchers.StringMatchers.startsWith;

public class OptionalMatcherTests {

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
                    JavaTest.test("Empty Optional", () -> that(Optional.empty(), isEmptyOptional())),
                    test("Optional Of", () -> that(Optional.of(10), isOptionalOf(10))),
                    test("Optional That", () -> that(Optional.of("Hello World"), isOptionalThat(startsWith("Hello"))))
            );
        }
    }

    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Empty Optional (FAIL)", () -> that(Optional.of("Hello"), isEmptyOptional())),
                    test("Optional Of (FAIL)", () -> that(Optional.of(10), isOptionalOf(20))),
                    test("Optional Of (FAIL - Empty)", () -> that(Optional.empty(), isOptionalOf(20))),
                    test("Optional That (FAIL)", () -> that(Optional.of("Hello World"), isOptionalThat(startsWith("Goodbye")))),
                    test("Optional That (FAIL - Empty)", () -> that(Optional.empty(), isOptionalThat(startsWith("Goodbye"))))
            );
        }
    }
}
