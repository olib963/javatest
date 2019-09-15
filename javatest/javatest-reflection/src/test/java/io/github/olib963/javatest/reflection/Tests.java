package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.JavaTest;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.matchers.Matcher.*;

import java.io.File;

public class Tests {

    public static void main(String... args) {
        var testSourceRoot = new File("javatest/javatest-reflection/src/test/java");
        var standardTest = test("Reflection runner finds all classes on classpath", () -> {
            var reflectivelyRunThisPackage = ReflectionRunners.forTestSourceRoot(testSourceRoot, Tests.class.getClassLoader())
                    .filter(p -> p.startsWith("io.github.olib963.javatest.reflection"));
            var results = run(reflectivelyRunThisPackage.runners());
            return that(!results.succeeded, "Tests should have failed overall")
                    .and(that("Total test count is correct", results.testCount(), isEqualTo(6L)))
                    .and(that("Passing test count is correct", results.successCount, isEqualTo(2L)))
                    .and(that("Failing test count is correct", results.failureCount, isEqualTo(1L)))
                    .and(that("Pending test count is correct", results.pendingCount, isEqualTo(3L)))
                    .and(that("There should be a run log for the failed fixture", results.allLogs().count(), isEqualTo(1L)));
        });

        var instantiationFailTest = test("Failure to instantiate a class returns a failed result", () -> {
            var reflectivelyRunTheFailingPackage = ReflectionRunners.forTestSourceRoot(testSourceRoot, Tests.class.getClassLoader())
                    .filter(p -> p.startsWith("failing"));
            var results = run(reflectivelyRunTheFailingPackage.runners());
            return that(!results.succeeded, "Tests should have failed overall")
                    .and(that("Total test count is correct", results.testCount(), isEqualTo(0L)))
                    .and(that("Passing test count is correct", results.successCount, isEqualTo(0L)))
                    .and(that("Failing test count is correct", results.failureCount, isEqualTo(0L)))
                    .and(that("Pending test count is correct", results.pendingCount, isEqualTo(0L)))
                    .and(that("Each failure added a run log", results.allLogs().count(), isEqualTo(2L)));
        });

        var classLoaderFailTest = test("Failure to load a class returns a failed result", JavaTest::pending);

        if(!runTests(standardTest, instantiationFailTest, classLoaderFailTest).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
    }

}
