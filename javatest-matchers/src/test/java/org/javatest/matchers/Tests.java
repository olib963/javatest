package org.javatest.matchers;

import org.javatest.JavaTest;
import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tests implements TestProvider {

    public static void main(String... args) {
        if(!JavaTest.run(new Tests()).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Passing Tests", () -> {
                    var tests = allTestsFrom(
                            SimpleMatcherTests.passing(),
                            StringMatcherTests.passing(),
                            ExceptionMatcherTests.passing(),
                            OptionalMatcherTests.passing(),
                            ComparableMatcherTests.passing(),
                            CollectionMatcherTests.passing(),
                            MapMatcherTests.passing());
                    var result = JavaTest.run(tests);
                    return that(result.succeeded, "Expected all 'passing' tests to pass");
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
        );
    }
}
