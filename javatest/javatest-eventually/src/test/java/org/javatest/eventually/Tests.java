package org.javatest.eventually;

import org.javatest.fixtures.Fixtures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class Tests {
    public static void main(String... args) {
        var result = runTests(Stream.of(
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

        var delayTests = Fixtures.fixtureRunner(
                "Executor Service",
                Fixtures.definitionFromThrowingFunctions(Executors::newSingleThreadExecutor, ExecutorService::shutdown),
                es -> testableRunner(new InitialDelayTests(es))
        );

        if (!run(delayTests).succeeded) {
            throw new RuntimeException("Delay tests failed!");
        }
        System.out.println("Tests passed");
    }
}
