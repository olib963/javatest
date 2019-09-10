package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.fixtures.Fixtures.*;
import static io.github.olib963.javatest.fixtures.Try.Failure;
import static io.github.olib963.javatest.fixtures.Try.Success;

public class SimpleTests implements TestSuiteClass {

    @Override
    public Stream<Testable> testables() {
        return Stream.of(
                JavaTest.test("Tests using fixture", () -> {
                    var result = run(fixtureRunner(
                            "string fixture",
                            definitionFromFunction(() -> Success("fixture")),
                            s -> testableRunner(List.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    var testCount = result.testCount();
                    return that(result.succeeded, "Expected tests to pass using fixture")
                            .and(that(testCount == 1, "Correct number of tests were run (expected 1, got " + testCount +")"));
                }),
                test("Failure to create fixture", () -> {
                    var result = run(fixtureRunner(
                            "fail to create",
                            definitionFromFunction(() -> Failure("Could not create fixture")),
                            s -> testableRunner(List.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    var testCount = result.testCount();
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be created")
                            .and(that(testCount == 0, "Correct number of tests were run (expected 0, got " + testCount +")"));
                }),
                test("Failure to destroy fixture", () -> {
                    var result = run(fixtureRunner(
                            "fail to destroy",
                            definitionFromFunctions(() -> Success("fixture"), s -> Failure("Could not destroy fixture")),
                            s -> testableRunner(List.of(
                                    test("Testing with " + s, () -> that(true, "this test should pass"))
                            ))
                    ));
                    var testCount = result.testCount();
                    return that(!result.succeeded, "Expected tests to fail if the fixture cannot be destroyed")
                            .and(that(testCount == 1, "Correct number of tests were run (expected 1, got " + testCount +")"));
                }),
                test("FixtureDefinition is fine but tests fail", () -> {
                    var result = run(fixtureRunner(
                            "test failures",
                            definitionFromFunction(() -> Success("fixture")),
                            s -> testableRunner(List.of(
                                    test("Testing with " + s, () -> that(false, "this test should fail"))
                            ))
                    ));
                    var testCount = result.testCount();
                    return that(!result.succeeded, "Expected tests to fail when the fixture is fine but internal tests fail")
                            .and(that(testCount == 1, "Correct number of tests were run (expected 1, got " + testCount +")"));
                })
        );
    }
}