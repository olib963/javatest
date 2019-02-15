package org.javatest.matchers;

import org.javatest.JavaTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.run(Stream.of(
                test("Passing Tests", () -> {
                    var tests = allTestsFrom(
                            SimpleMatcherTests.passing(),
                            StringMatcherTests.passing(),
                            ExceptionMatcherTests.passing(),
                            OptionalMatcherTests.passing(),
                            ComparableMatcherTests.passing(),
                            CollectionMatcherTests.passing(),
                            MapMatcherTests.passing());
                    var results = JavaTest.run(tests);
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var tests = allTestsFrom(
                            SimpleMatcherTests.failing(),
                            StringMatcherTests.failing(),
                            ExceptionMatcherTests.failing(),
                            OptionalMatcherTests.failing(),
                            ComparableMatcherTests.failing(),
                            CollectionMatcherTests.failing(),
                            MapMatcherTests.failing());
                    var results = tests.map(t -> JavaTest.run(Stream.of(t)));
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
