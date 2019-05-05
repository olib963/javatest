package org.javatest.fixtures.internal;

import org.javatest.fixtures.FixtureDefinition;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemporaryDirectory implements FixtureDefinition<File> {
    private final String filePath;

    public TemporaryDirectory(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public File create() throws Exception {
        var dir = new File(filePath);
        if (dir.mkdirs()) {
            return dir;
        }
        throw new Exception("Could not create temporary directory " + dir.getAbsolutePath());
    }

    @Override
    public void destroy(File fixture) throws Exception {
        var failedToDelete = deleteAll(fixture);
        var allFailedFiles = failedToDelete.collect(Collectors.joining(", "));
        if (!allFailedFiles.isEmpty()) {
            throw new Exception("Could not delete files: " + allFailedFiles);
        }
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
