package org.javatest.matchers;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.List;
import java.util.stream.Stream;

public class ExceptionMatcherTests {
    private static final List<String> tags = List.of("exception-matchers");
    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestProvider, ExceptionMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Exception of correct type is thrown", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasType(RuntimeException.class))), tags),
                    test("Exception has correct message", () ->
                            that(new RuntimeException("whoopsie"), hasMessage("whoopsie")), tags),
                    test("Exception with correct message is thrown", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessage("whoopsie"))), tags)
            );
        }
    }

    static class FailingTests implements TestProvider, ExceptionMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Exception of wrong type is thrown (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasType(IllegalStateException.class))), tags),
                    test("Exception has incorrect message (FAIL)", () ->
                            that(new RuntimeException("whoopsie"), hasMessage("BOOM")), tags),
                    test("Exception with incorrect message is thrown (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessage("BOOM"))), tags)
            );
        }
    }
}
