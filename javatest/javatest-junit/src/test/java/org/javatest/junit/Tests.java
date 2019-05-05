package org.javatest.junit;

import org.javatest.JavaTest;

import java.util.stream.Stream;

import static org.javatest.JavaTest.test;
import static org.javatest.JavaTest.that;

public class Tests {

    public static void main(String... args) {
        // TODO expand the JUnit tests
        // TODO expose the counts of run, pass, fail so that we can test the amounts
        var result = JavaTest.runTests(Stream.of(
                test("Passing tests pass", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("org.javatest.junit.passing"));
                    return that(r.succeeded, "Passing tests should have passed");
                }),
                test("Failing tests fail", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("org.javatest.junit"));
                    return that(!r.succeeded, "Total result should be a failure");
                })
        ));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }

}
