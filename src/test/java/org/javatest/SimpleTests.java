package org.javatest;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.stream.Stream;

public class SimpleTests {

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
                    test("Simple test", () -> that(true)),
                    test("Simple test with description", () -> that(true, "Expected true to be true")),
                    test("Pending test that has yet to be written", this::pending),
                    test("Pending test that has yet to be written", () -> pending("That has a description")),
                    test("And test", () -> that(true).and(that(true))),
                    test("Or test 1", () -> that(true).or(that(true))),
                    test("Or test 2", () -> that(false).or(that(true))),
                    test("Or test 3", () -> that(true).or(that(false))),
                    test("Xor test 1", () -> that(false).xor(that(true))),
                    test("Xor test 2", () -> that(true).xor(that(false))),
                    test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
                    test("Object is the correct type", () -> that("Hello", hasType(String.class)))
            );
        }
    }
    static class FailingTests implements TestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple test (FAIL)", () ->  that(false)),
                    test("Simple test with description (FAIL)", () ->  that(false, "Expected true to be true")),
                    test("And test 1 (FAIL)", () -> that(false).and(that(false))),
                    test("And test 2 (FAIL)", () -> that(true).and(that(false))),
                    test("And test 3 (FAIL)", () -> that(false).and(that(true))),
                    test("Or test (FAIL)", () -> that(false).or(that(false))),
                    test("Xor test 1 (FAIL)", () -> that(true).xor(that(true))),
                    test("Xor test 2 (FAIL)", () -> that(false).xor(that(false))),
                    test("Test throwing exception (FAIL)", () -> { throw new RuntimeException("This is an error"); }),
                    test("Test throwing assertion error (FAIL)", () -> { throw new AssertionError("This is an 'assertion'"); }),
                    test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                    test("Object is the incorrect type (FAIL)", () -> that("Hello", hasType(int.class)))
            );
        }
    }
}
