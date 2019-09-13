package io.github.olib963.javatest;

import io.github.olib963.javatest.documentation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.olib963.javatest.JavaTest.*;

public class AllTests {

    public static void main(String... args) {
        var simpleTests = SimpleTests.passing();
        var failingTests = suite("Failing Tests", SimpleTests.FAILING.map(t ->
                test(t.name, () -> {
                    var results = run(List.of(testableRunner(t)), Collections.emptyList());
                    return that(!results.succeeded, t.name + " should fail");
                })).collect(Collectors.toList()));
        var loggingTests = new LoggingTests();
        var result = run(testableRunner(List.of(simpleTests, failingTests, loggingTests)));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var extraDocRunners = new RunnerDocumentation().new MyRunners();
        var lazyDocRunners = new RunnerDocumentation().new LazyRunners();
        var simpleDocRunner = testableRunner(List.of(
                new AllDocumentationTests(),
                MyFirstTestSuite.mySuite(),
                new ClassAsSuite(),
                SuiteOfSuites.compositeSuite(),
                new MyPendingTests()
        ));
        var docResult = run(List.of(simpleDocRunner, extraDocRunners.singleTestRunner, extraDocRunners.suiteTestsNoLogging, lazyDocRunners.lazyRunner));
        if (!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }
}
