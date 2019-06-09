package io.github.olib963.javatest.junit;

import io.github.olib963.javatest.JavaTest;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.runTests(Stream.of(
                test("Passing tests pass", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit.passing"));
                    return that(r.succeeded, "Passing tests should have passed");
                }),
                test("Failing tests fail", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit"));
                    return that(!r.succeeded, "Total result should be a failure");
                })
        ));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }

}
