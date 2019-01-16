package org.javatest;

import static org.javatest.Assertion.*;
import static org.javatest.matchers.Matcher.*;

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
            new Test("Simple test", new Assertion.SimpleAssertion(true)),
            new Test("One add One is Two!", that(1 + 1, isEqualTo(2))),
            new Test("Exception of correct type is thrown", that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(RuntimeException.class)))
    );
    private static Stream<Test> failingTests = Stream.of(
            new Test("Simple test (FAIL)", new Assertion.SimpleAssertion(false)),
            new Test("One add One is Three! (FAIL)", that(1 + 1, isEqualTo(3))),
            new Test("Exception of wrong type is thrown (FAIL)", that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(IllegalStateException.class)))
    );
}
