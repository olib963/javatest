package org.javatest;

import java.util.stream.Stream;

public class FixtureTests implements TestProvider {

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Tests using fixture", () -> {
                    var result = JavaTest.run(JavaTest.fixtureRunner(
                            "string fixture",
                            () -> "fixture",
                            s -> {},
                            s -> Stream.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            )
                    ));
                    return that(result.succeeded, "Expected tests to pass using fixture");
                }),
                test("Failure to create fixture", () -> {
                    var result = JavaTest.run(JavaTest.fixtureRunner(
                            "fail to create",
                            () -> { throw new RuntimeException("Could not create fixture"); },
                            s -> {},
                            s -> Stream.empty()
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be created");
                }),
                test("Failure to destory fixture", () -> {
                    var result = JavaTest.run(JavaTest.fixtureRunner(
                            "fail to destroy",
                            () -> "fixture",
                            s -> { throw new RuntimeException("Could not destroy fixture"); },
                            s -> Stream.empty()
                    ));
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be destroyed");
                }),
                test("Fixture is fine but tests fail", () -> {
                    var result = JavaTest.run(JavaTest.fixtureRunner(
                            "test failures",
                            () -> "fixture",
                            s -> {},
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

