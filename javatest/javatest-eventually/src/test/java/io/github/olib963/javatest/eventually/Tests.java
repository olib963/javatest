package io.github.olib963.javatest.eventually;

import io.github.olib963.javatest.eventually.documentation.ConfigDocumentationTests;
import io.github.olib963.javatest.eventually.documentation.EventualTest;
import io.github.olib963.javatest.fixtures.FixtureDefinition;
import io.github.olib963.javatest.fixtures.Fixtures;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    private static FixtureDefinition<ExecutorService> executorFixture =
            Fixtures.definitionFromThrowingFunctions(Executors::newSingleThreadExecutor, ExecutorService::shutdown);

    public static void main(String... args) {
        var result = runTests(List.of(
                EventuallyTests.passing(),
                suite("Failing Tests",  EventuallyTests.FAILING.map(t ->
                        test(t.name, () -> {
                            var results = run(Stream.of(testableRunner(t)), Collections.emptyList());
                            return that(!results.succeeded, t.name + " should fail");
                        })
                ))
        ));
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var delayTests = Fixtures.fixtureRunner("Executor Service", executorFixture,
                es -> testableRunner(InitialDelayTests.delayTest(es)));

        if (!run(delayTests).succeeded) {
            throw new RuntimeException("Delay tests failed!");
        }

        var documentationTests = Fixtures.fixtureRunner("Executor Service", executorFixture,
                es -> testableRunner(List.of(new ConfigDocumentationTests(), EventualTest.eventualTest(es))));

        if (!run(documentationTests).succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }
}
