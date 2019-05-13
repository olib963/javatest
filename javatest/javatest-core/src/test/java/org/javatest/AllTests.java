package org.javatest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class AllTests {

    public static void main(String... args) {
        var result = JavaTest.runTests(Stream.of(
                test("Passing Tests", () -> {
                    var results = run(testableRunner(SimpleTests.passing()));
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var results = SimpleTests.FAILING.map(t -> JavaTest.runTests(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty(), "Expected all 'failing' tests to fail");
                })));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}
