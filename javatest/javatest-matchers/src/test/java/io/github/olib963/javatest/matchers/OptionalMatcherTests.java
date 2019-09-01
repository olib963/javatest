package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.OptionalMatchers.*;
import static io.github.olib963.javatest.matchers.StringMatchers.startsWith;

public class OptionalMatcherTests {
    public static Testable.TestSuite suite() {
        return Utils.matcherSuite("Optional Matcher Tests",
                Stream.of(
                        test("Empty Optional", () -> that(Optional.empty(), isEmptyOptional())),
                        test("Optional Of", () -> that(Optional.of(10), isOptionalOf(10))),
                        test("Optional That", () -> that(Optional.of("Hello World"), isOptionalThat(startsWith("Hello"))))
                ),
                Stream.of(
                        test("Empty Optional (FAIL)", () -> that(Optional.of("Hello"), isEmptyOptional())),
                        test("Optional Of (FAIL)", () -> that(Optional.of(10), isOptionalOf(20))),
                        test("Optional Of (FAIL - Empty)", () -> that(Optional.empty(), isOptionalOf(20))),
                        test("Optional That (FAIL)", () -> that(Optional.of("Hello World"), isOptionalThat(startsWith("Goodbye")))),
                        test("Optional That (FAIL - Empty)", () -> that(Optional.empty(), isOptionalThat(startsWith("Goodbye"))))
                )
        );
    }
}
