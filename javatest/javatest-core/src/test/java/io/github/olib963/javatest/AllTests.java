package io.github.olib963.javatest;

import io.github.olib963.javatest.documentation.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class AllTests {

    public static void main(String... args) {
        var result = runTests(Stream.of(
                test("Passing Tests", () -> {
                    var results = run(testableRunner(SimpleTests.passing()));
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var results = SimpleTests.FAILING.map(t -> runTests(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty(), "Expected all 'failing' tests to fail");
                })));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var extraDocRunners = new MyRunners();
        var simpleDocRunner = testableRunner(Stream.of(
                new AllDocumentationTests(),
                new MyFirstTestSuite(),
                new MyCustomTestSuite(),
                new MyPendingTests()
        ));
        var docResult = run(Stream.of(simpleDocRunner, extraDocRunners.singleTestRunner, extraDocRunners.suiteTestsNoLogging));
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
        System.out.println("Tests passed");
    }
}
