package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.List;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class ExceptionMatcherTests {
    private static final List<String> tags = List.of("exception-matchers");
    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements MatcherTestProvider, ExceptionMatchers, StringMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Exception of correct type is thrown", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasType(RuntimeException.class))), tags),
                    test("Exception has correct message", () ->
                            that(new RuntimeException("whoopsie"), hasMessage("whoopsie")), tags),
                    test("Exception with correct message is thrown", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessage("whoopsie"))), tags),
                    test("Exception has message containing substring", () ->
                            that(new RuntimeException("oh no!"), hasMessageThat(containsString("no"))), tags),
                    test("Exception is thrown with message containing substring", () ->
                            that(() -> { throw new RuntimeException("oh no!"); }, willThrowExceptionThat(hasMessageThat(containsString("no")))), tags),
                    test("Exception has correct cause", () -> {
                                var cause = new RuntimeException("Boo");
                                return that(new RuntimeException("oh no!", cause), hasCause(cause));
                            }, tags),
                    test("Exception is thrown with correct cause", () -> {
                                var cause = new RuntimeException("Boo");
                                return that(() -> { throw new RuntimeException("oh no!", cause); }, willThrowExceptionThat(hasCause(cause)));
                            }, tags),
                    test("Exception has cause with correct message", () ->
                            that(new RuntimeException("oh no!", new RuntimeException("Boo")), hasCauseThat(hasMessage("Boo"))), tags),
                    test("Exception is thrown with cause that has correct message", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("Boo")); }, willThrowExceptionThat(hasCauseThat(hasMessage("Boo")))), tags)
            );
        }
    }

    // TODO the latter tests errors don't compose nicely!
    static class FailingTests implements MatcherTestProvider, ExceptionMatchers, StringMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Exception is thrown (FAIL)", () ->
                            that(() -> {}, willThrowExceptionThat(hasType(IllegalStateException.class))), tags),
                    test("Exception of wrong type is thrown (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasType(IllegalStateException.class))), tags),
                    test("Exception has incorrect message (FAIL)", () ->
                            that(new RuntimeException("whoopsie"), hasMessage("BOOM")), tags),
                    test("Exception with incorrect message is thrown (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessage("BOOM"))), tags),
                    test("Exception message not containing substring (FAIL)", () ->
                            that(new RuntimeException("whoopsie"), hasMessageThat(containsString("no"))), tags),
                    test("Exception with message is thrown with message not containing substring (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessageThat(containsString("no")))), tags),
                    test("Exception has incorrect cause (FAIL)", () ->
                        that(new RuntimeException("oh no!", new RuntimeException("foo")), hasCause(new RuntimeException("bar"))), tags),
                    test("Exception is thrown with incorrect cause (FAIL)", () ->
                        that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCause(new RuntimeException("bar")))), tags),
                    test("Exception has cause with incorrect message (FAIL)", () ->
                            that(new RuntimeException("oh no!", new RuntimeException("foo")), hasCauseThat(hasMessage("bar"))), tags),
                    test("Exception is thrown with cause that has incorrect message (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCauseThat(hasMessage("bar")))), tags),
                    test("Exception is thrown with cause that has incorrect type (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCauseThat(hasType(IllegalStateException.class)))), tags),
                    test("Exception is thrown that has incorrect length of message (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!"); }, willThrowExceptionThat(hasMessageThat(hasLength(10)))), tags),
                    test("Exception is thrown with cause that has incorrect length of message (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCauseThat(hasMessageThat(hasLength(10))))), tags)
            );
        }
    }
}
