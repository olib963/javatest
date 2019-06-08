package io.github.olib963.javatest.benchmarks.internal;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

public class BenchmarkAssertion implements Assertion {
    private final Assertion assertion;
    private final Supplier<Duration> timeFunction;
    private final Function<Duration, String> formatter;
    public BenchmarkAssertion(Assertion assertion, Supplier<Duration> timeFunction, Function<Duration, String> formatter) {
        this.assertion = assertion;
        this.timeFunction = timeFunction;
        this.formatter = formatter;
    }

    @Override
    public AssertionResult run() {
        var result = assertion.run();
        var timeTaken = timeFunction.get();
        // TODO it would be nice to have more structured logs in tests rather than \n\t etc.
        // Maybe even somehow extensible with types?
        var timeLog = formatter.apply(timeTaken);
        return AssertionResult.of(result.holds, result.description + "\n\t" + timeLog);
    }
}
