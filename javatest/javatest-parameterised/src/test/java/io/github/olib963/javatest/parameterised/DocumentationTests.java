package io.github.olib963.javatest.parameterised;

import io.github.olib963.javatest.testable.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;
// tag::import[]
import static io.github.olib963.javatest.parameterised.Parameterised.*;
// end::import[]

public class DocumentationTests {

    // tag::palindrome[]
    public class PalindromeTests {
        public Stream<Test> inMemoryPalindromeTests() {
            // Create parameterised tests from an in memory stream
            return parameterised(
                    Stream.of("Civic", "Deified", "Kayak", "Level", "Madam"),
                    this::palindromeTest
            );
        }

        // Function accepting the word and defining the test
        private Test palindromeTest(String word) {
            return test(word + " is a palindrome", () -> {
                var lowercase = word.toLowerCase();
                var backwards = new StringBuilder(lowercase).reverse().toString();
                return that(lowercase.equals(backwards), word + " is the same backwards");
            });
        }

        // Stream larger data set from a file instead
        public Stream<Test> palindromeTestsFromFile() throws IOException {
            String path = getClass().getClassLoader().getResource("palindromes.txt").getPath();
            Stream<String> lines = Files.lines(Path.of(path));
            return parameterised(lines, this::palindromeTest);
        }
    }
    // end::palindrome[]

    private long fibonacci(int n) {
        if(n == 0) {
            return 0L;
        }
        return Stream.iterate(t(0L, 1L), // Start with (0, 1) which is F_0 and F_1
                tup -> t(tup._2, tup._1 + tup._2)) // F_n-1 and F_n
                .limit(n)
                .skip(n - 1)
                .findFirst()
                .map(t -> t._2)
                .orElse(1L);
    }

    // tag::fibonacci[]
    public class FibonacciTests {
        public Stream<Test> fibonacciTests() {
            return parameterised(
                    Stream.of(
                            // t() is the tuple constructor and can be applied to up to 10 arguments
                            t(0, 0L),
                            t(1, 1L),
                            t(2, 1L),
                            t(3, 2L),
                            t(4, 3L),
                            t(5, 5L),
                            t(6, 8L),
                            t(8, 21L),
                            t(10, 55L),
                            t(60, 1548008755920L),
                            t(90, 2880067194370816120L)),
                    // We then create a function with the same arity and types as our tuples, in this case: (Integer, Long)
                    (n, expectedFib) -> test(n + "th fibonacci number", () ->
                            that(fibonacci(n) == expectedFib, "The " + n + "th fibonacci number is " + expectedFib)));
        }
    }
    // end::fibonacci[]

    public static PalindromeTests palindromes() {
        return new DocumentationTests().new PalindromeTests();
    }

    public static FibonacciTests fibonacci() {
        return new DocumentationTests().new FibonacciTests();
    }

}

