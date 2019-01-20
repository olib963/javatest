package org.javatest;

import org.javatest.matchers.StringMatcherTests;
import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("All tests expected to pass should pass", () -> {
                    var tests = allTestsFrom(new SimpleTests(), StringMatcherTests.passing());
                    var result = JavaTest.run(tests);
                    return that(result.succeeded);
                }),
                test("All tests expected to fail should fail", () -> {
                    var tests = allTestsFrom(new SimpleFailingTests(), StringMatcherTests.failing());
                    var results = tests.map(t -> JavaTest.run(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty());
                })
        );
    }
}
