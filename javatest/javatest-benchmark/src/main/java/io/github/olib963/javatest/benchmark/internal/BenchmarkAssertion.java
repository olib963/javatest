package io.github.olib963.javatest.benchmark.internal;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BenchmarkAssertion implements Assertion {
    private final Assertion assertion;
    private final Supplier<Duration> timeFunction;
    private final Function<Duration, String> formatter;
    private final Optional<Duration> testLimit;

    public BenchmarkAssertion(Assertion assertion,
                              Supplier<Duration> timeFunction,
                              Function<Duration, String> formatter,
                              Optional<Duration> testLimit) {
        this.assertion = assertion;
        this.timeFunction = timeFunction;
        this.formatter = formatter;
        this.testLimit = testLimit;
    }

    @Override
    public AssertionResult run() {
        var result = assertion.run();
        var timeTaken = timeFunction.get();
        // TODO it would be nice to have more structured logs in tests rather than Strings
        // Maybe even somehow extensible with types like some heterogeneous context? This may make aggregating easier since the types would be corrects
        var timeLog = "Test took " + formatter.apply(timeTaken);
        var wrappedAssertion = AssertionResult.of(result.holds, result.description, Collections.singletonList(timeLog));
        return testLimit
                .map(limit -> compareTimeToLimit(limit, timeTaken, wrappedAssertion))
                .orElse(wrappedAssertion);
    }

    private AssertionResult compareTimeToLimit(Duration limit, Duration timeTaken, AssertionResult wrappedAssertion) {
        if (timeTaken.compareTo(limit) > 0) {
            var newLogs = Stream.concat(wrappedAssertion.logs(),
                    Stream.of("but should have taken no longer than " + formatter.apply(limit)))
                    .collect(Collectors.toList());
            return AssertionResult.of(false, wrappedAssertion.description, newLogs);
        } else {
            return wrappedAssertion;
        }
    }
}
