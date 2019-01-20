package org.javatest;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.stream.Stream;

public class SimpleTests {

    public static TestProvider passing() {
        return new SimplePassingTests();
    }
    public static TestProvider failing() {
        return new SimpleFailingTests();
    }

    static class SimplePassingTests implements TestProvider {

        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple test", () -> that(true)),
                    test("Simple test with description", () -> that(true, "Expected true to be true")),
                    test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
                    test("Exception of correct type is thrown", () -> that(() -> {
                        throw new RuntimeException("whoopsie");
                    }, willThrow(RuntimeException.class))),
                    test("Pending test that has yet to be written", this::pending),
                    test("And test", () -> that(true).and(that(true))),
                    test("Or test 1", () -> that(true).or(that(true))),
                    test("Or test 2", () -> that(false).or(that(true))),
                    test("Or test 3", () -> that(true).or(that(false))),
                    test("Xor test 1", () -> that(false).xor(that(true))),
                    test("Xor test 2", () -> that(true).xor(that(false)))
            );
        }
    }
    static class SimpleFailingTests implements TestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple test (FAIL)", () ->  that(false)),
                    test("Simple test with description (FAIL)", () ->  that(false, "Expected true to be true")),
                    test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                    test("Exception of wrong type is thrown (FAIL)", () -> that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(IllegalStateException.class))),
                    test("And test 1 (FAIL)", () -> that(false).and(that(false))),
                    test("And test 2 (FAIL)", () -> that(true).and(that(false))),
                    test("And test 3 (FAIL)", () -> that(false).and(that(true))),
                    test("Or test (FAIL)", () -> that(false).or(that(false))),
                    test("Xor test 1 (FAIL)", () -> that(true).xor(that(true))),
                    test("Xor test 2 (FAIL)", () -> that(false).xor(that(false))),
                    test("Test throwing exception (FAIL)", () -> { throw new RuntimeException("This is an error"); }),
                    test("Test throwing assertion error (FAIL)", () -> { throw new AssertionError("This is an 'assertion'"); })
            );
        }
    }
}
