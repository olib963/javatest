package org.javatest;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class AllTests {

    public static void main(String... args) {
        var result = JavaTest.run(Stream.of(
                test("Passing Tests", () -> {
                    var results = JavaTest.run(allTestsFrom(
                            SimpleTests.passing(),
                            new FixtureTests()));
                    return that(results.succeeded, "Expected all 'passing' tests to pass");
                }),
                test("Failing Tests", () -> {
                    var results = SimpleTests.failing().testStream().map(t -> JavaTest.run(Stream.of(t)));
                    var passingTests = results.filter(r -> r.succeeded).collect(Collectors.toList());
                    return that(passingTests.isEmpty(), "Expected all 'failing' tests to fail");
                })));
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var integrationResult = JavaTest.run(
                JavaTest.fixtureRunnerFromProvider(
                        "test directory",
                        AllTests::createTestDirectory,
                        AllTests::recursiveDelete,
                        IntegrationTests::new
                )
        );
        if (!integrationResult.succeeded) {
            throw new RuntimeException("Integration tests failed!");
        }
        System.out.println("Tests passed");
    }

    private static File createTestDirectory() {
        var dir = new File("integraton-test");
        if (dir.mkdirs()) {
            return dir;
        }
        throw new IllegalStateException("Could not create integration test directory " + dir.getAbsolutePath());
    }

    private static void recursiveDelete(File file) {
        if(file.isDirectory()){
            var files = file.listFiles();
            if(files != null) {
                Arrays.stream(files).forEach(AllTests::recursiveDelete);
            }
        }
        if (!file.delete()) {
            throw new IllegalStateException("Could not delete file " + file.getAbsolutePath());
        }
    }

}
