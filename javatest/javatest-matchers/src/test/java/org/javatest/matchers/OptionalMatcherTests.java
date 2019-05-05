package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;
import static org.javatest.matchers.Matcher.*;
import static org.javatest.matchers.OptionalMatchers.*;
import static org.javatest.matchers.StringMatchers.*;

public class OptionalMatcherTests {
    private static final List<String> tags = List.of("optional-matchers");

    public static TestProvider passing() {
        return new PassingTests();
    }

    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Empty Optional", () -> that(Optional.empty(), isEmptyOptional()), tags),
                    test("Optional Of", () -> that(Optional.of(10), isOptionalOf(10)), tags),
                    test("Optional That", () -> that(Optional.of("Hello World"), isOptionalThat(startsWith("Hello"))), tags)
            );
        }
    }

    static class FailingTests implements TestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Empty Optional (FAIL)", () -> that(Optional.of("Hello"), isEmptyOptional()), tags),
                    test("Optional Of (FAIL)", () -> that(Optional.of(10), isOptionalOf(20)), tags),
                    test("Optional Of (FAIL - Empty)", () -> that(Optional.empty(), isOptionalOf(20)), tags),
                    test("Optional That (FAIL)", () -> that(Optional.of("Hello World"), isOptionalThat(startsWith("Goodbye"))), tags),
                    test("Optional That (FAIL - Empty)", () -> that(Optional.empty(), isOptionalThat(startsWith("Goodbye"))), tags)
            );
        }
    }
}
