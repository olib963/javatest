package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.TestRunner;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.matchers.Matcher.*;

import java.io.File;
import java.util.Collection;

public class Tests {

    public static void main(String... args) {
        var standardTest = test("Reflection runner finds all classes on classpath", () -> {
            // tag::include[]
            File testSourceRoot = new File("javatest/javatest-reflection/src/test/java");

            Collection<TestRunner> reflectivelyRunThisPackage =
                    ReflectionRunners.forTestSourceRoot(testSourceRoot, Tests.class.getClassLoader())
                            .filterClasses(c -> c.startsWith("io.github.olib963.javatest.reflection"))
                            .runners();
            // end::include[]
            var results = run(reflectivelyRunThisPackage);
            return that(!results.succeeded, "Tests should have failed overall")
                    .and(that("Total test count is correct", results.testCount(), isEqualTo(6L)))
                    .and(that("Passing test count is correct", results.successCount, isEqualTo(2L)))
                    .and(that("Failing test count is correct", results.failureCount, isEqualTo(1L)))
                    .and(that("Pending test count is correct", results.pendingCount, isEqualTo(3L)))
                    .and(that("There should be a run log for the failed fixture", results.allLogs().count(), isEqualTo(1L)));
        });
        var testSourceRoot = new File("javatest/javatest-reflection/src/test/java");
        var instantiationFailTest = test("Failure to instantiate a class returns a failed result", () -> {
            var reflectivelyRunTheFailingPackage = ReflectionRunners.forTestSourceRoot(testSourceRoot, Tests.class.getClassLoader())
                    .filterClasses(c -> c.startsWith("failing"));
            var results = run(reflectivelyRunTheFailingPackage.runners());
            return that(!results.succeeded, "Tests should have failed overall")
                    .and(that("Total test count is correct", results.testCount(), isEqualTo(0L)))
                    .and(that("Passing test count is correct", results.successCount, isEqualTo(0L)))
                    .and(that("Failing test count is correct", results.failureCount, isEqualTo(0L)))
                    .and(that("Pending test count is correct", results.pendingCount, isEqualTo(0L)))
                    .and(that("Each failure added a run log", results.allLogs().count(), isEqualTo(2L)));
        });

        var classLoaderFailTest = test("Failure to load a class returns a failed result", () -> {
            var reflectivelyAttemptThisPackage = ReflectionRunners.forTestSourceRoot(testSourceRoot, new FailingClassLoader())
                    .filterClasses(c -> c.startsWith("io.github.olib963.javatest.reflection"));
            var results = run(reflectivelyAttemptThisPackage.runners());
            return that(!results.succeeded, "Tests should have failed overall")
                    .and(that("Total test count is correct", results.testCount(), isEqualTo(0L)))
                    .and(that("Passing test count is correct", results.successCount, isEqualTo(0L)))
                    .and(that("Failing test count is correct", results.failureCount, isEqualTo(0L)))
                    .and(that("Pending test count is correct", results.pendingCount, isEqualTo(0L)))
                    // There are 8 classes in this package. They have to be loaded before they can be filtered on subtype so there are 8 failures instead of 6.
                    .and(that("Each failure added a run log", results.allLogs().count(), isEqualTo(8L)));
        });

        if(!runTests(standardTest, instantiationFailTest, classLoaderFailTest).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
    }

}
