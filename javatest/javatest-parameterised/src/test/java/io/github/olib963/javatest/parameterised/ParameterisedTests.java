package io.github.olib963.javatest.parameterised;

import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.testable.Test;
import io.github.olib963.javatest.TestSuiteClass;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;
import static io.github.olib963.javatest.parameterised.Parameterised.parameterised;

public class ParameterisedTests implements TestSuiteClass {
    @Override
    public Stream<Testable> testables() {
        return Stream.of(
                parameterised(Data.multiplication(), this::multiplicationTest),
                parameterised(Data.madAddition(), this::additionTest)
        ).flatMap(Function.identity());
    }

    private Test multiplicationTest(int m1, int m2, int eq) {
        return test("Multiplication", () -> that(m1 * m2 == eq, m1 + " multiplied by " + m2 + " equals " + eq));
    }

    // Yeah I know in reality this would just be (Seq[Int], Int), but the point was to show the use of 10 parameters
    private Test additionTest(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int eq) {
        var numbers = Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        return test("Addition", () ->
                that(numbers.stream().mapToInt(i -> i).sum() == eq,
                        "The sum of " + numbers + " equals " + eq));
    }
}
