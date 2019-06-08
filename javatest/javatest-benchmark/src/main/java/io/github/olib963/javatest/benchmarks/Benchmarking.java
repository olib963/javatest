package io.github.olib963.javatest.benchmarks;

import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.benchmarks.internal.BenchmarkAssertion;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Benchmarking {

    static final Function<Duration, String> DEFAULT_FORMAT = d ->
            String.format("Test took %ss %sms", d.getSeconds(), d.toMillisPart());

    public static Test benchmark(Test test) {
        return benchmark(test, DEFAULT_FORMAT);
    }

    public static Test benchmark(Test test, Function<Duration, String> formatter) {
        return new Test(test.name, () -> {
            var startMillis = System.currentTimeMillis();
            var assertion = test.test.get();
            return new BenchmarkAssertion(assertion, createTimer(startMillis), formatter);
        });
    }

    private static Supplier<Duration> createTimer(long startMillis) {
        return () -> Duration.ofMillis(System.currentTimeMillis() - startMillis);
    }

    public static Stream<Testable> benchmarkAllTests(Stream<Testable> testables) {
        return benchmarkAllTests(testables, DEFAULT_FORMAT);
    }

    public static Stream<Testable> benchmarkAllTests(Stream<Testable> testables, Function<Duration, String> formatter) {
        return testables.map(t -> new Testable() {
            @Override
            public Optional<String> suiteName() {
                return t.suiteName();
            }

            @Override
            public Stream<Test> tests() {
                return t.tests().map(test -> benchmark(test, formatter));
            }
        });
    }
}
