package org.javatest.eventually;

import org.javatest.JavaTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javatest.JavaTest.test;
import static org.javatest.JavaTest.that;

public class Tests {
    public static void main(String... args) {
        var result = JavaTest.runTests(Stream.of(
                test("Passing Tests", () -> {
                    var results = JavaTest.runTests(EventuallyTests.passing().testStream());
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var results = EventuallyTests.failing().testStream().map(t -> JavaTest.runTests(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty(), "Expected all 'failing' tests to fail");
                })
        ));
        if(!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}
