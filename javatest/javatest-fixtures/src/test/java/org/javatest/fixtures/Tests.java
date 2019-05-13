package org.javatest.fixtures;

import static org.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = run(testableRunner(new SimpleTests()));
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