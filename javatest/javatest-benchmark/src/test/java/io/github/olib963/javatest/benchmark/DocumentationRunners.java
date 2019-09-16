package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.TestRunners;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.testableRunner;
import static io.github.olib963.javatest.benchmark.Benchmark.benchmark;

public class DocumentationRunners implements TestRunners {
    @Override
    public Collection<TestRunner> runners() {
        return List.of(benchmark(Stream.of(testableRunner(new DocumentationTests().testSuite()))));
    }
}
