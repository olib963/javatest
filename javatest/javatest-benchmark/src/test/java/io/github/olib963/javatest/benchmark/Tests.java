package io.github.olib963.javatest.benchmark;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = runTests(new BenchmarkingTests());
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var docResult = runTests(new DocumentationTests().testSuite());
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }
}
