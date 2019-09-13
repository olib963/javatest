package io.github.olib963.javatest.benchmark;

import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.Testable.*;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

// tag::imports[]
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.benchmark.Benchmark.*;
// end::imports[]

public class DocumentationTests {

    // tag::simple[]
    public class MyBenchMarks {
        private Test simpleTest = test("Some test I have", () -> that(true, "Passing test"));
        public Test timedTest = benchmark(simpleTest);

        public Collection<Testable> bunchOfTimedTests = benchmarkAllTests(
                List.of(new MyFirstSuite(), new MySecondSuite(), simpleTest)
        );
    }
    // end::simple[]

    private class MyFirstSuite extends BenchmarkingTests {}
    private class MySecondSuite extends BenchmarkingTests {}

    // tag::limit[]
    public class MyLimitedTest {
        private Test longTest = test("Some long test", () -> {
            Thread.sleep(5000l);
            return that(true, "Ordinarily this would pass.");
        });

        public Test failingTest = failIfLongerThan(Duration.ofSeconds(4), longTest);
    }
    // end::limit[]

    // tag::customFormat[]
    public class MyCustomBenchmark {
        private Test simpleTest = test("Some test I have", () -> that(true, "Passing test"));

        private Function<Duration, String> format = d -> d.toMillis() + "milliseconds";

        public Test timedTestCustomFormat = benchmark(simpleTest, format);

    }
    // end::customFormat[]

    public Testable testSuite() {
        var bM = new MyBenchMarks();
        return suite("Documentation", List.of(
                test("Passing tests pass", () -> {
                    var timedTests = bM.bunchOfTimedTests;
                    timedTests.add(bM.timedTest);
                    timedTests.add(new MyCustomBenchmark().timedTestCustomFormat);
                    var results = run(testableRunner(timedTests));
                    return that(results.succeeded, "All tests passed");
                }),
                test("Failing test fails", () -> {
                    var results = runTests(List.of(new MyLimitedTest().failingTest));
                    return that(!results.succeeded, "Test failed");
                })
        ));
    }

}
