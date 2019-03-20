package org.javatest.fixtures;

import org.javatest.JavaTest;

import java.io.File;
import java.util.Arrays;

public class Tests {

    public static void main(String... args) {
        var result = JavaTest.run(new SimpleTests());
        if (!result.succeeded) {
            throw new RuntimeException("Unit tests failed!");
        }

        var integrationResult = JavaTest.run(
                Fixtures.Runners.fixtureRunnerFromProvider(
                        "test directory",
                        Tests::createTestDirectory,
                        Tests::recursiveDelete,
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
        if (file.isDirectory()) {
            var files = file.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(Tests::recursiveDelete);
            }
        }
        if (!file.delete()) {
            throw new IllegalStateException("Could not delete file " + file.getAbsolutePath());
        }
    }
}