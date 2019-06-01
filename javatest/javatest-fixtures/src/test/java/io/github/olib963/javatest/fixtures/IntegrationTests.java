package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;
import io.github.olib963.javatest.javafire.JavaTest;
import io.github.olib963.javatest.javafire.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class IntegrationTests implements TestSuite {
    private final File testDirectory;

    public IntegrationTests(File testDirectory) {
        this.testDirectory = testDirectory;
    }

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                JavaTest.test("Exists", () ->
                        that(testDirectory.isDirectory(), "Expected test file to be a directory")
                                .and(that(testDirectory.exists(), "Expected test directory to exist"))),
                test("Writing to test file", () -> {
                    var testFile = new File(testDirectory, "test.txt");
                    var path = testFile.toPath();
                    var testData = List.of("hello world", "goodbye world");
                    Files.write(path, testData);
                    var contents = Files.lines(path).collect(Collectors.toList());
                    return that(testData.equals(contents), "Written file should contain correct data");
                })
        );
    }
}