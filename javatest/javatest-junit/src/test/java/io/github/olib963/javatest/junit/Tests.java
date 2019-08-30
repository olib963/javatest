package io.github.olib963.javatest.junit;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.junit.documentation.JUnitRunners;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.runTests(Stream.of(
                test("Passing testables pass", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit.passing"));
                    return that(r.succeeded, "Passing testables should have passed");
                }),
                test("Failing testables fail", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit"));
                    return that(!r.succeeded, "Total result should be a failure");
                })
        ));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var docResult = JavaTest.run(JUnitRunners.junitRunners().stream());
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation testables failed!");
        }
    }

}
