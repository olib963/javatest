package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.fixtures.documentation.MyRunners;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

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

    public static void main(String... args) throws InterruptedException, ExecutionException {
        var result = JavaTest.run(testableRunner(new SimpleTests()));
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var integrationResult = run(integrationTestRunner);
        if (!integrationResult.succeeded) {
            throw new RuntimeException("Integration tests failed!");
        }

        var docResult = JavaTest.run(testableRunner(CustomDefinitions.tests()));
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }

        // Main README documentation makes use of Fixtures so the documentation file is here rather than in core
        // Default ForkJoinPool does not seem to want to shutdown safely when using parallel streams :/
        // TODO look into this, it would suck for clients to have to do this. Perhaps we can provide a better parallelism API
        var forkJoinPool = new ForkJoinPool(2);
        try {
            var quickStartResult = forkJoinPool.submit(() -> JavaTest.run(new MyRunners().runners())).get();
            if (!quickStartResult.succeeded) {
                throw new RuntimeException("Quickstart documentation tests failed!");
            }
        } finally {
          forkJoinPool.shutdownNow();
        }
    }

}