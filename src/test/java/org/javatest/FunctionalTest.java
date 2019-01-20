package org.javatest;

import org.javatest.matchers.StringMatcherTests;
import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionalTest implements TestProvider {
    public static void main(String... args) {
        if(!JavaTest.run(passingTests).succeeded) {
            throw new RuntimeException("Tests expected to pass failed!");
        };

        failingTests.forEach(test -> {
            if(JavaTest.run(Stream.of(test)).succeeded) {
                throw new RuntimeException("Test expected to fail passed!");
            }
        });

        System.out.println("Functional Tests passed");
    }


    private static Stream<Test> passingTests = Stream.concat(
            new SimpleTests().passingTests,
            new StringMatcherTests().passingTests
    );

    private static Collection<Test> failingTests = Stream.concat(
            new SimpleTests().failingTests,
            new StringMatcherTests().failingTests
    ).collect(Collectors.toList());
}
