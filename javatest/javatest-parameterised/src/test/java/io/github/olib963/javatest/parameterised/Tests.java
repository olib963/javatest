package io.github.olib963.javatest.parameterised;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) throws IOException {
        var result = run(testableRunner(new ParameterisedTests()));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");

        var docs = new DocumentationTests();
        var allDocTests = Stream.of(
                docs.inMemoryPalindromeTests(),
                docs.palindromeTestsFromFile(),
                docs.fibonacciTests()
        ).flatMap(Function.identity());
        var docResult = runTests(allDocTests);
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
        System.out.println("Documentation tests passed");
    }
}
