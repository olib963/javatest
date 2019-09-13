package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Testable.Test;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.benchmark.internal.BenchmarkAssertion;
import io.github.olib963.javatest.benchmark.internal.BenchmarkRunner;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Benchmark {

    private Benchmark(){}

    static final Function<Duration, String> DEFAULT_FORMAT = d ->
            String.format("%ss %sms", d.getSeconds(), d.toMillisPart());

    public static Test benchmark(Test test) {
        return benchmark(test, DEFAULT_FORMAT);
    }

    public static Test failIfLongerThan(Duration limit, Test test) {
        return failIfLongerThan(limit, test, DEFAULT_FORMAT);
    }

    public static Test failIfLongerThan(Duration limit, Test test, Function<Duration, String> formatter) {
        return benchmarkTest(test, formatter, Optional.of(limit));
    }

    public static Test benchmark(Test test, Function<Duration, String> formatter) {
        return benchmarkTest(test, formatter, Optional.empty());
    }

    private static Test benchmarkTest(Test test, Function<Duration, String> formatter, Optional<Duration> limit) {
        return JavaTest.test(test.name, () -> {
            var startMillis = System.currentTimeMillis();
            var assertion = test.test.get();
            return new BenchmarkAssertion(assertion, createTimer(startMillis), formatter, limit);
        });
    }

    private static Supplier<Duration> createTimer(long startMillis) {
        return () -> Duration.ofMillis(System.currentTimeMillis() - startMillis);
    }

    public static Collection<Testable> benchmarkAllTests(Collection<Testable> testables) {
        return benchmarkAllTests(testables, DEFAULT_FORMAT);
    }

    public static Collection<Testable> benchmarkAllTests(Collection<? extends Testable> testables, Function<Duration, String> formatter) {
        return testables.stream().map(t -> t.match(
                test -> benchmark(test, formatter),
                testSuite -> JavaTest.suite(testSuite.name, benchmarkAllTests(testSuite.testables().collect(Collectors.toList()), formatter))
        )).collect(Collectors.toList());
    }

    public static TestRunner benchmark(Stream<TestRunner> runners) {
        return benchmark(runners, DEFAULT_FORMAT);
    }

    public static TestRunner benchmark(Stream<TestRunner> runners, Function<Duration, String> formatter) {
        return new BenchmarkRunner(runners, formatter, System::currentTimeMillis, s -> Duration.ofMillis(System.currentTimeMillis() - s));
    }
}
