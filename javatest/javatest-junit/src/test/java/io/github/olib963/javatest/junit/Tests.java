package io.github.olib963.javatest.junit;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.junit.documentation.JUnitRunners;

import java.util.List;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.runTests(List.of(
                test("Passing tests pass", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit.passing"));
                    return that(r.succeeded, "Passing tests should have passed")
                            .and(that(r.testCount() == 1, "Only one test was run"))
                            .and(that(r.pendingCount == 0, "No tests are pending"))
                            .and(that(r.failureCount == 0, "No tests failed"))
                            .and(that(r.successCount == 1, "One test passed"));
                }),
                test("Failing tests fail", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit"));
                    return that(!r.succeeded, "Total result should be a failure")
                            .and(that(r.testCount() == 2, "Only two tests were run"))
                            .and(that(r.pendingCount == 0, "No tests are pending"))
                            .and(that(r.failureCount == 1, "One test failed"))
                            .and(that(r.successCount == 1, "One test passed"));
                })
        ));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var docResult = JavaTest.run(JUnitRunners.junitRunners());
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }

}
