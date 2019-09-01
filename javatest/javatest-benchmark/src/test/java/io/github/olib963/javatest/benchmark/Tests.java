package io.github.olib963.javatest.benchmark;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.benchmark.Benchmark.benchmark;

public class Tests {

    public static void main(String... args) {
        var result = runTests(new BenchmarkingTests());
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var docRunners = Stream.of(testableRunner(new DocumentationTests().testSuite()));
        var docResult = run(benchmark(docRunners));
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }
}
