package org.javatest;

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
                test("All tests expected to pass should pass", () -> {
                    var result = JavaTest.run(SimpleTests.passing().testStream());
                    return that(result.succeeded);
                }),
                test("All tests expected to fail should fail", () -> {
                    var results = SimpleTests.failing().testStream().map(t -> JavaTest.run(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty());
                })
        );
    }
}
