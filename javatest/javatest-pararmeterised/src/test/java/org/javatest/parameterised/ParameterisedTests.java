package org.javatest.parameterised;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.javatest.parameterised.Helpers.t;

public class ParameterisedTests implements TestProvider, Parameterised {
    @Override
    public Stream<Test> testStream() {
        // TODO create joinStreams function
        return Stream.of(
                parameterised(Data.palindromes(), word -> test(word + " is a palindrome", () -> {
                    var lowercase = word.toLowerCase();
                    var backwards = new StringBuilder(lowercase).reverse().toString();
                    return that(lowercase.equals(backwards), word + " is the same backwards");
                })),
                parameterised(Data.multiplication(), (m1, m2, eq) ->
                        test("Multiplication", () -> that(m1 * m2 == eq,
                                m1 + " multiplied by " + m2 + " equals " + eq))),
                parameterised(Data.fibonacci(), (n, fib) ->
                        test(n + "th fibonacci number", () ->
                                that(fibonacci(n) == fib, "The " + n + "th fibonacci number is " + fib)))
        ).flatMap(Function.identity());
    }

    private long fibonacci(int n) {
        return Stream.iterate(t(1L, 1L), // Start with (1, 1) which is F_1 and F_2
                tup -> t(tup._2, tup._1 + tup._2)) // F_n-1 and F_n
                .limit(Integer.max(n - 1, 0))
                .skip(Integer.max(n - 2, 0))
                .findFirst()
                .map(t -> t._2)
                .orElse(1L);
    }
}
