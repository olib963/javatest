package io.github.olib963.javatest.fixtures.documentation;

// tag::include[]
import io.github.olib963.javatest.*;
import io.github.olib963.javatest.fixtures.Fixtures;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class MyRunners implements TestRunners {

    @Override
    public Stream<TestRunner> runners() {
        // Define a runner for unit tests in parallel
        List<Testable> tests = List.of(new MyFirstUnitTestSuite(), new MySecondUnitTestSuite());
        var unitTests = lazyTestableRunner(tests.parallelStream());

        // Define integration tests with an executor fixture
        var executorDefinition = Fixtures.definitionFromThrowingFunctions(
                Executors::newSingleThreadExecutor, ExecutorService::shutdown);

        var integrationTests = Fixtures.fixtureRunner("executor",
                executorDefinition,
                es -> testableRunner(new MyIntegrationTestSuite(es)));
        // Run both
        return Stream.of(unitTests, integrationTests);
    }

}
// end::include[]
