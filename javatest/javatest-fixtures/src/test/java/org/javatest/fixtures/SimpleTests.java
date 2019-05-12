package org.javatest.fixtures;

import org.javatest.Test;
import org.javatest.TestSuite;

import java.util.stream.Stream;

import static org.javatest.JavaTest.*;
import static org.javatest.fixtures.Fixtures.*;
import static org.javatest.fixtures.Try.*;

public class SimpleTests implements TestSuite {

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Tests using fixture", () -> {
                    var result = run(fixtureRunner(
                            "string fixture",
                            definitionFromFunction(() -> Success("fixture")),
                            s -> testStreamRunner(Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    return that(result.succeeded, "Expected tests to pass using fixture");
                }),
                test("Failure to create fixture", () -> {
                    var result = run(fixtureRunner(
                            "fail to create",
                            definitionFromFunction(() -> Failure("Could not create fixture")),
                            s -> testStreamRunner(Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be created");
                }),
                test("Failure to destroy fixture", () -> {
                    var result = run(fixtureRunner(
                            "fail to destroy",
                            definitionFromFunctions(() -> Success("fixture"), s -> Failure("Could not destroy fixture")),
                            s -> testStreamRunner(Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be destroyed");
                }),
                test("FixtureDefinition is fine but tests fail", () -> {
                    var result = run(fixtureRunner(
                            "test failures",
                            definitionFromFunction(() -> Success("fixture")),
                            s -> testStreamRunner(Stream.of(
                                    test("Testing with " + s, () -> that(false, "this test should fail"))
                            ))
                    ));
                    // TODO should we check the failure number?
                    return that(!result.succeeded, "Expected tests to fail when the fixture is fine but internal tests fail");
                })
        );
    }
}