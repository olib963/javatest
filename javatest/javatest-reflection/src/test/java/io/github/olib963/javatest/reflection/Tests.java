package io.github.olib963.javatest.reflection;

import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.matchers.Matcher.*;

import java.io.File;

public class Tests {

    public static void main(String... args) {
        var test = test("Reflection runner finds all classes on classpath", () -> {
            var results = run(new ReflectionRunners(new File("javatest/javatest-reflection/src/test/java"), Tests.class.getClassLoader()).runners());
            return that(!results.succeeded, "Tests should have failed overall")
                    .and(that("Total test count is correct", results.testCount(), isEqualTo(6L)))
                    .and(that("Passing test count is correct", results.successCount, isEqualTo(2L)))
                    .and(that("Failing test count is correct", results.failureCount, isEqualTo(1L)))
                    .and(that("Pending test count is correct", results.pendingCount, isEqualTo(3L)));
        });

        if(!runTests(test).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
    }

}
