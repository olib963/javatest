package org.javatest;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.stream.Stream;

public class SimpleTests implements TestProvider {

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Simple test", () -> that(true)),
                test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
                test("Exception of correct type is thrown", () ->  that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(RuntimeException.class))),
                test("Pending test that has yet to be written", () ->  pending()),
                test("And test", () -> that(true).and(that(true))),
                test("Or test 1", () -> that(true).or(that(true))),
                test("Or test 2", () -> that(false).or(that(true))),
                test("Or test 3", () -> that(true).or(that(false))),
                test("Xor test 1", () -> that(false).xor(that(true))),
                test("Xor test 2", () -> that(true).xor(that(false)))
        );
    }
}
