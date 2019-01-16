package org.javatest;

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

    private static Stream<Test> passingTests = Stream.of(new Test("test", new Assertion.SimpleAssertion(true)));
    private static Stream<Test> failingTests = Stream.of(new Test("failingtest", new Assertion.SimpleAssertion(false)));
}
