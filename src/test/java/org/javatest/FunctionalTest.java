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
                throw new RuntimeException("Tests expected to fail passed!");
            }
        });

        System.out.println("Functional Tests passed");
    }

    private static Stream<Test> passingTests = Stream.of(
            new Test("test", new Assertion.SimpleAssertion(true)),
            new Test("One add One is Two!", that(1 + 1, isEqualTo(2)))
    );
    private static Stream<Test> failingTests = Stream.of(new Test("failing-test", new Assertion.SimpleAssertion(false)));
}
