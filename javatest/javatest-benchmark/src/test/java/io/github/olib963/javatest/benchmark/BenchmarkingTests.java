package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.*;
import io.github.olib963.javatest.benchmark.internal.BenchmarkAssertion;
import io.github.olib963.javatest.benchmark.internal.BenchmarkRunner;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;
import static io.github.olib963.javatest.matchers.CollectionMatchers.contains;
import static io.github.olib963.javatest.matchers.Matcher.that;

public class BenchmarkingTests implements TestSuiteClass {

    private static final Assertion PASS = that(true, "Assertion passes");

    @Override
    public Collection<Testable> testables() {
        return List.of(
                test("Formatted duration", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofMillis(12345);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmark.DEFAULT_FORMAT, Optional.empty());
                    var logs = benchmarked.run().logs().collect(Collectors.toList());
                    return that(logs, contains("Test took 12s 345ms"));
                }),
                test("Longer than expected", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofSeconds(10);
                    var limit = Duration.ofSeconds(9);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmark.DEFAULT_FORMAT, Optional.of(limit));
                    var result = benchmarked.run();
                    var logs = benchmarked.run().logs().collect(Collectors.toList());
                    return that(!result.holds, "Assertion should fail because test took too long")
                            .and(that(logs, contains("but should have taken no longer than 9s 0ms")));
                }),
                test("Quicker than expected", () -> {
                    Supplier<Duration> timeFunction = () -> Duration.ofSeconds(8);
                    var limit = Duration.ofSeconds(9);
                    var benchmarked = new BenchmarkAssertion(PASS, timeFunction, Benchmark.DEFAULT_FORMAT, Optional.of(limit));
                    var result = benchmarked.run();
                    return that(result.holds, "Assertion should pass when test runs within limit");
                }),
                test("Runner adds log", () -> {
                    TestRunner mockRunner = c -> TestResults.empty();
                    var benchmarked = new BenchmarkRunner(Stream.of(mockRunner), Benchmark.DEFAULT_FORMAT, () -> 0L, t -> Duration.ofMillis(987654321));
                    var results = benchmarked.run(RunConfiguration.empty());
                    return that(results.allLogs().collect(Collectors.toList()), contains("Test run took 987654s 321ms"));
                })
        );
    }
}
