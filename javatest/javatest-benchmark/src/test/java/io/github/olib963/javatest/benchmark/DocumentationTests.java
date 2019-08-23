package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;
import io.github.olib963.javatest.Testable;

import java.time.Duration;
import java.util.function.Function;
import java.util.stream.Stream;

// tag::imports[]
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.benchmark.Benchmark.*;
// end::imports[]

public class DocumentationTests implements TestSuite {

    // tag::simple[]
    private Test simpleTest = test("Some test I have", () -> that(true, "Passing test"));
    public Test timedTest = benchmark(simpleTest);

    public Stream<Testable> bunchOfTimedTests = benchmarkAllTests(
            Stream.of(new MyFirstSuite(), new MySecondSuite(), simpleTest)
    );
    // end::simple[]

    private class MyFirstSuite extends BenchmarkingTests {}
    private class MySecondSuite extends BenchmarkingTests {}

    // tag::limit[]
    private Test longTest = test("Some long test", () -> {
        Thread.sleep(5000l);
        return that(true, "Ordinarily this would pass.");
    });

    public Test failingTest = failIfLongerThan(longTest, Duration.ofSeconds(4));
    // end::limit[]

    // tag::customFormat[]
    private Function<Duration, String> format = d -> d.toMillis() + "milliseconds";

    public Test timedTestCustomFormat = benchmark(simpleTest, format);
    // end::customFormat[]

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                test("Passing tests pass", () -> {
                    var singleTimedTests = Stream.of(timedTest, timedTestCustomFormat);
                    var results = run(testableRunner(Stream.concat(bunchOfTimedTests, singleTimedTests)));
                    return that(results.succeeded, "All tests passed");
                }),
                test("Failing test fails", () -> {
                    var results = runTests(Stream.of(failingTest));
                    return that(!results.succeeded, "Test failed");
                })
        );
    }

}
