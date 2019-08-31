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

        var palindromes = suite("Palindrome Tests", Stream.concat(
                DocumentationTests.palindromes().inMemoryPalindromeTests(),
                DocumentationTests.palindromes().palindromeTestsFromFile()
        ));
        var fibonacci = suite("Finonacci Tests", DocumentationTests.fibonacci().fibonacciTests());
        var docResult = runTests(palindromes, fibonacci);
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }
}
