package io.github.olib963.javatest.junit;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.javafire.TestRunners;
import io.github.olib963.javatest.junit.documentation.JUnitRunners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests implements TestRunners {

    @Override
    public Collection<TestRunner> runners() {
        var unitTests = JavaTest.testableRunner(List.of(
                test("Passing tests pass", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit.passing"));
                    return all(that(r.succeeded, "Passing tests should have passed"),
                            that(r.testCount() == 1, "Only one test was run"),
                            that(r.pendingCount == 0, "No tests are pending"),
                            that(r.failureCount == 0, "No tests failed"),
                            that(r.successCount == 1, "One test passed"));
                }),
                test("Failing tests fail", () -> {
                    var r = JavaTest.run(JUnitTestRunner.fromPackage("io.github.olib963.javatest.junit"));
                    return all(that(!r.succeeded, "Total result should be a failure"),
                            that(r.testCount() == 2, "Only two tests were run"),
                            that(r.pendingCount == 0, "No tests are pending"),
                            that(r.failureCount == 1, "One test failed"),
                            that(r.successCount == 1, "One test passed"));
                })
        ));

        var documentationTests = JUnitRunners.junitRunners();
        var allRunners = new ArrayList<TestRunner>();
        allRunners.add(unitTests);
        allRunners.addAll(documentationTests);
        return allRunners;
    }
}
