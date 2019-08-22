package io.github.olib963.javatest.eventually;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.eventually.documentation.ConfigDocumentationTests;
import io.github.olib963.javatest.eventually.documentation.EventualTest;
import io.github.olib963.javatest.fixtures.FixtureDefinition;
import io.github.olib963.javatest.fixtures.Fixtures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    private static FixtureDefinition<ExecutorService> executorFixture =
            Fixtures.definitionFromThrowingFunctions(Executors::newSingleThreadExecutor, ExecutorService::shutdown);

    public static void main(String... args) {
        var result = JavaTest.runTests(Stream.of(
                test("Passing Tests", () -> {
                    var results = run(testableRunner(EventuallyTests.passing()));
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var results = EventuallyTests.FAILING.map(t -> runTests(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty(), "Expected all 'failing' tests to fail");
                })
        ));
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }


        var delayTests = Fixtures.fixtureRunner("Executor Service", executorFixture,
                es -> testableRunner(new InitialDelayTests(es)));

        if (!run(delayTests).succeeded) {
            throw new RuntimeException("Delay tests failed!");
        }
        System.out.println("Tests passed");

        var documentationTests = Fixtures.fixtureRunner("Executor Service", executorFixture,
                es -> testableRunner(Stream.of(new ConfigDocumentationTests(), EventualTest.eventualTest(es))));

        if (!run(documentationTests).succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
        System.out.println("Tests passed");
    }
}
