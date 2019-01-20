package org.javatest;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.stream.Stream;

public class SimpleFailingTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Simple test (FAIL)", () ->  that(false)),
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
