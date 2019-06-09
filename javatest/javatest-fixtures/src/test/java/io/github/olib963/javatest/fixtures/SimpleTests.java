package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.fixtures.Fixtures.*;
import static io.github.olib963.javatest.fixtures.Try.Failure;
import static io.github.olib963.javatest.fixtures.Try.Success;

public class SimpleTests implements TestSuite {

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                JavaTest.test("Tests using fixture", () -> {
                    var result = run(fixtureRunner(
                            "string fixture",
                            definitionFromFunction(() -> Success("fixture")),
                            s -> testableRunner(Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    return that(result.succeeded, "Expected tests to pass using fixture");
                }),
                test("Failure to create fixture", () -> {
                    var result = run(fixtureRunner(
                            "fail to create",
                            definitionFromFunction(() -> Failure("Could not create fixture")),
                            s -> testableRunner(Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be created");
                }),
                test("Failure to destroy fixture", () -> {
                    var result = run(fixtureRunner(
                            "fail to destroy",
                            definitionFromFunctions(() -> Success("fixture"), s -> Failure("Could not destroy fixture")),
                            s -> testableRunner(Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be destroyed");
                }),
                test("FixtureDefinition is fine but tests fail", () -> {
                    var result = run(fixtureRunner(
                            "test failures",
                            definitionFromFunction(() -> Success("fixture")),
                            s -> testableRunner(Stream.of(
                                    test("Testing with " + s, () -> that(false, "this test should fail"))
                            ))
                    ));
                    return that(!result.succeeded, "Expected tests to fail when the fixture is fine but internal tests fail");
                })
        );
    }
}