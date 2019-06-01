package io.github.olib963.javatest.fixtures.internal;

import io.github.olib963.javatest.fixtures.FixtureDefinition;
import io.github.olib963.javatest.fixtures.Try;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.fixtures.Try.Failure;
import static io.github.olib963.javatest.fixtures.Try.Success;

public class TemporaryDirectory implements FixtureDefinition<File> {
    private final String filePath;

    public TemporaryDirectory(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Try<File> create() {
        var dir = new File(filePath);
        if (dir.mkdirs()) {
            return Success(dir);
        }
        return Failure("Could not create temporary directory " + dir.getAbsolutePath());
    }

    @Override
    public Try<Void> destroy(File fixture) {
        var failedToDelete = deleteAll(fixture);
        var allFailedFiles = failedToDelete.collect(Collectors.joining(", "));
        if (!allFailedFiles.isEmpty()) {
            return Failure("Could not delete files: " + allFailedFiles);
        }
        return Success();
    }

    private Stream<String> deleteAll(File file) {
        return allFilesIn(file).flatMap(f -> {
            if (f.delete()) {
                return Stream.empty();
            }
            return Stream.of(f.getAbsolutePath());
        });
    }

    private Stream<File> allFilesIn(File file) {
        var thisFile = Stream.of(file);
        var files = file.listFiles();
        if (file.isDirectory() && files != null) {
            return Stream.concat(
                    Arrays.stream(files).flatMap(this::allFilesIn),
                    thisFile);
        }
        return thisFile;
    }
}
