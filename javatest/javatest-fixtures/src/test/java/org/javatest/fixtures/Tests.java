package org.javatest.fixtures;

import org.javatest.JavaTest;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.run(new SimpleTests());
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var integrationResult = JavaTest.run(
                Fixtures.fixtureRunnerFromProvider(
                        "test directory",
                        Fixtures.temporaryDirectory("integraton-test"),
                        IntegrationTests::new
                )
        );
        if (!integrationResult.succeeded) {
            throw new RuntimeException("Integration tests failed!");
        }
        System.out.println("Tests passed");
    }

}