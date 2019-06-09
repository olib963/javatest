package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.ExceptionMatchers.*;
import static io.github.olib963.javatest.matchers.Matcher.hasType;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.StringMatchers.containsString;
import static io.github.olib963.javatest.matchers.StringMatchers.hasLength;

public class ExceptionMatcherTests {
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
                    JavaTest.test("Exception of correct type is thrown", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasType(RuntimeException.class)))),
                    test("Exception has correct message", () ->
                            that(new RuntimeException("whoopsie"), hasMessage("whoopsie"))),
                    test("Exception with correct message is thrown", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessage("whoopsie")))),
                    test("Exception has message containing substring", () ->
                            that(new RuntimeException("oh no!"), hasMessageThat(containsString("no")))),
                    test("Exception is thrown with message containing substring", () ->
                            that(() -> { throw new RuntimeException("oh no!"); }, willThrowExceptionThat(hasMessageThat(containsString("no"))))),
                    test("Exception has correct cause", () -> {
                                var cause = new RuntimeException("Boo");
                                return that(new RuntimeException("oh no!", cause), hasCause(cause));
                            }),
                    test("Exception is thrown with correct cause", () -> {
                                var cause = new RuntimeException("Boo");
                                return that(() -> { throw new RuntimeException("oh no!", cause); }, willThrowExceptionThat(hasCause(cause)));
                            }),
                    test("Exception has cause with correct message", () ->
                            that(new RuntimeException("oh no!", new RuntimeException("Boo")), hasCauseThat(hasMessage("Boo")))),
                    test("Exception is thrown with cause that has correct message", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("Boo")); }, willThrowExceptionThat(hasCauseThat(hasMessage("Boo")))))
            );
        }
    }

    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Exception is thrown (FAIL)", () ->
                            that(() -> {}, willThrowExceptionThat(hasType(IllegalStateException.class)))),
                    test("Exception of wrong type is thrown (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasType(IllegalStateException.class)))),
                    test("Exception has incorrect message (FAIL)", () ->
                            that(new RuntimeException("whoopsie"), hasMessage("BOOM"))),
                    test("Exception with incorrect message is thrown (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessage("BOOM")))),
                    test("Exception message not containing substring (FAIL)", () ->
                            that(new RuntimeException("whoopsie"), hasMessageThat(containsString("no")))),
                    test("Exception with message is thrown with message not containing substring (FAIL)", () ->
                            that(() -> { throw new RuntimeException("whoopsie"); }, willThrowExceptionThat(hasMessageThat(containsString("no"))))),
                    test("Exception has incorrect cause (FAIL)", () ->
                        that(new RuntimeException("oh no!", new RuntimeException("foo")), hasCause(new RuntimeException("bar")))),
                    test("Exception is thrown with incorrect cause (FAIL)", () ->
                        that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCause(new RuntimeException("bar"))))),
                    test("Exception has cause with incorrect message (FAIL)", () ->
                            that(new RuntimeException("oh no!", new RuntimeException("foo")), hasCauseThat(hasMessage("bar")))),
                    test("Exception is thrown with cause that has incorrect message (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCauseThat(hasMessage("bar"))))),
                    test("Exception is thrown with cause that has incorrect type (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCauseThat(hasType(IllegalStateException.class))))),
                    test("Exception is thrown that has incorrect length of message (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!"); }, willThrowExceptionThat(hasMessageThat(hasLength(10))))),
                    test("Exception is thrown with cause that has incorrect length of message (FAIL)", () ->
                            that(() -> { throw new RuntimeException("oh no!", new RuntimeException("foo")); }, willThrowExceptionThat(hasCauseThat(hasMessageThat(hasLength(10))))))
            );
        }
    }
}
