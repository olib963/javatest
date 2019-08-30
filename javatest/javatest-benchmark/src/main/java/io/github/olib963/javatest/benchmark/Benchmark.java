package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.testable.Test;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.benchmark.internal.BenchmarkAssertion;
import io.github.olib963.javatest.benchmark.internal.BenchmarkRunner;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Benchmark {

    private Benchmark(){}

    static final Function<Duration, String> DEFAULT_FORMAT = d ->
            String.format("%ss %sms", d.getSeconds(), d.toMillisPart());

    public static Test benchmark(Test test) {
        return benchmark(test, DEFAULT_FORMAT);
    }

    public static Test failIfLongerThan(Test test, Duration limit) {
        return failIfLongerThan(test, limit, DEFAULT_FORMAT);
    }

    public static Test failIfLongerThan(Test test, Duration limit, Function<Duration, String> formatter) {
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

    public static Stream<Testable> benchmarkAllTests(Stream<Testable> testables) {
        return benchmarkAllTests(testables, DEFAULT_FORMAT);
    }

    public static Stream<Testable> benchmarkAllTests(Stream<Testable> testables, Function<Duration, String> formatter) {
        // TODO need to implement this one
//        return testables.map(t -> new Testable() {
//            @Override
//            public Optional<String> suiteName() {
//                return t.suiteName();
//            }
//
//            @Override
//            public Stream<Test> tests() {
//                return t.tests().map(test -> benchmark(test, formatter));
//            }
//        });
        return testables;
    }

    public static TestRunner benchmark(Stream<TestRunner> runners) {
        return benchmark(runners, DEFAULT_FORMAT);
    }

    public static TestRunner benchmark(Stream<TestRunner> runners, Function<Duration, String> formatter) {
        return new BenchmarkRunner(runners, formatter, System::currentTimeMillis, s -> Duration.ofMillis(System.currentTimeMillis() - s));
    }
}
