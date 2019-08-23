package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestRunner;

import static io.github.olib963.javatest.JavaTest.run;
import static io.github.olib963.javatest.JavaTest.testableRunner;

public class Tests {

    // tag::runner[]
    private static TestRunner integrationTestRunner =
            Fixtures.fixtureRunner(
                    // Name the fixture
                    "test directory",

                    // Use the existing temporary directory fixture
                    Fixtures.temporaryDirectory("integration-test"),

                    // Define a function to create your test runner that uses the fixture
                    d -> testableRunner(IntegrationTests.tests(d))
            );
    // end::runner[]

    public static void main(String... args) {
        var result = JavaTest.run(testableRunner(new SimpleTests()));
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var integrationResult = run(integrationTestRunner);
        if (!integrationResult.succeeded) {
            throw new RuntimeException("Integration tests failed!");
        }
        System.out.println("Tests passed");

        var docResult = JavaTest.run(testableRunner(CustomDefinitions.tests()));
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }

}