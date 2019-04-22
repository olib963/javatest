package org.javatest.fixtures;

import org.javatest.JavaTest;
import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.stream.Stream;
import static org.javatest.fixtures.Fixtures.*;

public class SimpleTests implements TestProvider {

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Tests using fixture", () -> {
                    var result = JavaTest.run(fixtureRunner(
                            "string fixture",
                            fromFunction(() -> "fixture"),
                            s -> Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            )
                    ));
                    return that(result.succeeded, "Expected tests to pass using fixture");
                }),
                test("Failure to create fixture", () -> {
                    var result = JavaTest.run(fixtureRunner(
                            "fail to create",
                            fromFunction(() -> { throw new RuntimeException("Could not create fixture"); }),
                            s -> Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            )
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be created");
                }),
                test("Failure to destroy fixture", () -> {
                    var result = JavaTest.run(fixtureRunner(
                            "fail to destroy",
                            fromFunctions(() -> "fixture", s -> { throw new RuntimeException("Could not destroy fixture"); }),
                            s -> Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            )
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be destroyed");
                }),
                test("Fixture is fine but tests fail", () -> {
                    var result = JavaTest.run(fixtureRunner(
                            "test failures",
                            fromFunction(() -> "fixture"),
                            s -> Stream.of(
                                    test("Testing with " + s, () -> that(false, "this test should fail"))
                            )
                    ));
                    // TODO should we check the failure number?
                    return that(!result.succeeded, "Expected tests to fail when the fixture is fine but internal tests fail");
                })
        );
    }
}