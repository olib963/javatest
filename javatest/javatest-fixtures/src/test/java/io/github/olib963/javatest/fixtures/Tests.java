package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.javafire.JavaTest;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.run(testableRunner(new SimpleTests()));
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var integrationResult = run(
                Fixtures.fixtureRunner(
                        "test directory",
                        Fixtures.temporaryDirectory("integration-test"),
                        d -> testableRunner(new IntegrationTests(d))
                )
        );
        if (!integrationResult.succeeded) {
            throw new RuntimeException("Integration tests failed!");
        }
        System.out.println("Tests passed");
    }

}