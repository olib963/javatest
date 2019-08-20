package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.*;
import io.github.olib963.javatest.benchmark.internal.BenchmarkAssertion;
import io.github.olib963.javatest.benchmark.internal.BenchmarkRunner;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.StringMatchers.containsString;
import static io.github.olib963.javatest.matchers.CollectionMatchers.contains;

public class BenchmarkingTests implements TestSuite {

    private static final Assertion PASS = that(true, "Assertion passes");

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                test("Formatted duration", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofMillis(12345);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmark.DEFAULT_FORMAT, Optional.empty());
                    var description = benchmarked.run().description;
                    return that(description, containsString("Test took 12s 345ms"));
                }),
                test("Longer than expected", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofSeconds(10);
                    var limit = Duration.ofSeconds(9);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmark.DEFAULT_FORMAT, Optional.of(limit));
                    var result = benchmarked.run();
                    return that(!result.holds, "Assertion should fail because test took too long")
                            .and(that(result.description, containsString("but should have taken no longer than 9s 0ms")));
                }),
                test("Quicker than expected", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofSeconds(8);
                    var limit = Duration.ofSeconds(9);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmark.DEFAULT_FORMAT, Optional.of(limit));
                    var result = benchmarked.run();
                    return that(result.holds, "Assertion should pass when test runs within limit");
                }),
                test("Runner adds log", () -> {
                    TestRunner mockRunner = TestResults::init;
                    var benchmarked = new BenchmarkRunner(Stream.of(mockRunner), Benchmark.DEFAULT_FORMAT, () -> 0L, t -> Duration.ofMillis(987654321));
                    var results = benchmarked.run();
                    return that(results.allLogs(), contains("Test run took 987654s 321ms"));
                })
        );
    }
}
