package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestSuite;
import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.javafire.JavaTest;
import io.github.olib963.javatest.javafire.TestSuite;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = runTests(Stream.of(
                test("Passing Tests", () -> {
                    var tests = Stream.<Testable>of(
                            SimpleMatcherTests.passing(),
                            StringMatcherTests.passing(),
                            ExceptionMatcherTests.passing(),
                            OptionalMatcherTests.passing(),
                            ComparableMatcherTests.passing(),
                            CollectionMatcherTests.passing(),
                            MapMatcherTests.passing());
                    var results = run(testableRunner(tests));
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var tests = Stream.of(
                            SimpleMatcherTests.failing(),
                            StringMatcherTests.failing(),
                            ExceptionMatcherTests.failing(),
                            OptionalMatcherTests.failing(),
                            ComparableMatcherTests.failing(),
                            CollectionMatcherTests.failing(),
                            MapMatcherTests.failing());
                    var results = tests.flatMap(TestSuite::tests).map(t -> runTests(Stream.of(t)));
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
