package org.javatest;

import static org.javatest.assertions.Assertion.*;
import static org.javatest.matchers.Matcher.*;
import static org.javatest.Test.*;

import java.util.*;
import java.util.stream.Stream;

public class FunctionalTest {
    public static void main(String... args) {
        if(!JavaTest.run(passingTests)) {
            throw new RuntimeException("Tests expected to pass failed!");
        };

        failingTests.forEach(test -> {
            if(JavaTest.run(Stream.of(test))) {
                throw new RuntimeException("Test expected to fail passed!");
            }
        });

        System.out.println("Functional Tests passed");
    }

    private static Stream<Test> passingTests = Stream.of(
            test("Simple test", () -> that(true)),
            test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
            test("Exception of correct type is thrown", () ->  that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(RuntimeException.class))),
            test("Pending test that has yet to be written", () ->  pending())
    );
    private static Collection<Test> failingTests = List.of(
            test("Simple test (FAIL)", () ->  that(false)),
            test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
            test("Exception of wrong type is thrown (FAIL)", () -> that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(IllegalStateException.class)))
    );
}
