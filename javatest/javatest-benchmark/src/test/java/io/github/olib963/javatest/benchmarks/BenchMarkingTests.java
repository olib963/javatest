package io.github.olib963.javatest.benchmarks;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;
import io.github.olib963.javatest.benchmarks.internal.BenchmarkAssertion;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.matchers.Matcher.*;
import static io.github.olib963.javatest.matchers.StringMatchers.*;

public class BenchMarkingTests implements TestSuite {

    private static final Assertion PASS = that(true, "Assertion passes");

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                test("Formatted duration", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofMillis(12345);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmarking.DEFAULT_FORMAT);
                    var description = benchmarked.run().description;
                    return that(description, containsString("took 12s 345ms"));
                }),
                test("Longer than expected", JavaTest::pending),
                test("Quicker than expected", JavaTest::pending),
                test("Runner adds log", JavaTest::pending)
        );
    }
}
