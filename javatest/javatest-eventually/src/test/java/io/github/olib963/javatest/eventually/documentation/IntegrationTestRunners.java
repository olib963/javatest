package io.github.olib963.javatest.eventually.documentation;

import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.javafire.TestRunners;
import io.github.olib963.javatest.eventually.InitialDelayTests;
import io.github.olib963.javatest.fixtures.FixtureDefinition;
import io.github.olib963.javatest.fixtures.Fixtures;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.olib963.javatest.JavaTest.*;

public class IntegrationTestRunners implements TestRunners {

    private static FixtureDefinition<ExecutorService> executorFixture =
            Fixtures.definitionFromThrowingFunctions(Executors::newSingleThreadExecutor, ExecutorService::shutdown);

    @Override
    public Collection<TestRunner> runners() {
        return List.of(
                Fixtures.fixtureRunner("Executor Service", executorFixture,
                        es -> testableRunner(
                                suite("Integration tests", List.of(
                                        InitialDelayTests.delayTest(es),
                                        EventualTest.eventualTest(es)
                                )))));
    }
}
