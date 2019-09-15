package io.github.olib963.javatest.reflection.runners;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.TestRunners;
import io.github.olib963.javatest.fixtures.FixtureDefinition;
import io.github.olib963.javatest.fixtures.Try;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.fixtures.Fixtures.*;

public class FailingRunners implements TestRunners {

    private FixtureDefinition<File> failingFixture = definitionFromFunction(() -> Try.Failure("Failed to create directory"));

    @Override
    public Collection<TestRunner> runners() {
        var integrationTests = fixtureRunner("temp dir", failingFixture, dir ->
                testableRunner(
                        suite("Temp dir tests",
                                List.of(
                                        test("Pending integration test", JavaTest::pending),
                                        test("Directory exists", () ->
                                                that(dir.isDirectory(), "temp dir is a directory")
                                                        .and(that(dir.exists(), "temp dir exists")))
                                ))));
        return Collections.singletonList(integrationTests);
    }
}
