package org.javatest.parameterised;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.javatest.parameterised.Parameterised.*;
import static org.javatest.JavaTest.*;

public class ParameterisedTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        // TODO create joinStreams function
        return Stream.of(
                parameterised(Data.palindromes(), this::palindromeTest),
                parameterised(Data.multiplication(), this::multiplicationTest),
                parameterised(Data.madAddition(), this::additionTest),
                parameterised(Data.fibonacci(), (n, fib) ->
                        test(n + "th fibonacci number", () ->
                                that(fibonacci(n) == fib, "The " + n + "th fibonacci number is " + fib)))

        ).flatMap(Function.identity());
    }

    private Test palindromeTest(String word) {
        return test(word + " is a palindrome", () -> {
            var lowercase = word.toLowerCase();
            var backwards = new StringBuilder(lowercase).reverse().toString();
            return that(lowercase.equals(backwards), word + " is the same backwards");
        });
    }

    private Test multiplicationTest(int m1, int m2, int eq) {
        return test("Multiplication", () -> that(m1 * m2 == eq, m1 + " multiplied by " + m2 + " equals " + eq));
    }

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

    // Yeah I know in reality this would just be (Seq[Int], Int), but the point was to show the use of 10 parameters
    private Test additionTest(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int eq) {
        var numbers = Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        return test("Addition", () ->
                that(numbers.stream().mapToInt(i -> i).sum() == eq,
                        "The sum of " + numbers + " equals " + eq));
    }
}
