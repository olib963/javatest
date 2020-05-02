package io.github.olib963.javatest.benchmark.internal;

import io.github.olib963.javatest.RunConfiguration;
import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BenchmarkRunner implements TestRunner {
    private final Stream<TestRunner> runners;
    private final Function<Duration, String> formatter;
    private final Supplier<Long> startMillisSupplier;
    private final Function<Long, Duration> timerFunction;

    public BenchmarkRunner(Stream<TestRunner> runners, Function<Duration, String> formatter, Supplier<Long> startMillisSupplier, Function<Long, Duration> timerFunction) {
        this.runners = runners;
        this.formatter = formatter;
        this.startMillisSupplier = startMillisSupplier;
        this.timerFunction = timerFunction;
    }

    @Override
    public TestResults run(RunConfiguration configuration) {
        var startMillis = startMillisSupplier.get();
        var results = runners.map(r -> r.run(configuration)).reduce(TestResults.empty(), TestResults::combine);
        var timeTaken = timerFunction.apply(startMillis);
        return results.addLog("Test run took " + formatter.apply(timeTaken));
    }
}
