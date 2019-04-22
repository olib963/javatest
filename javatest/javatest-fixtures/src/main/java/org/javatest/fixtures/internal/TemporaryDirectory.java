package org.javatest.fixtures.internal;

import org.javatest.fixtures.Fixture;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemporaryDirectory implements Fixture<File> {
    private final String filePath;

    public TemporaryDirectory(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public File create() {
        var dir = new File(filePath);
        if (dir.mkdirs()) {
            return dir;
        }
        throw new IllegalStateException("Could not create temporary directory " + dir.getAbsolutePath());
    }

    @Override
    public void destroy(File fixture) {
        var failedToDelete = deleteAll(fixture);
        if (!failedToDelete.isEmpty()) {
            throw new IllegalStateException("Could not delete files: " +
                    failedToDelete.stream().collect(Collectors.joining(", ")));
        }
    }

    private List<String> deleteAll(File file) {
        allFilesToDeleteIn(file).forEach(System.out::println);
        return allFilesToDeleteIn(file).flatMap(f -> {
            if (f.delete()) {
                return Stream.empty();
            }
            return Stream.of(f.getAbsolutePath());
        }).collect(Collectors.toList());
    }

    private Stream<File> allFilesToDeleteIn(File topLevel) {
        var thisFile = Stream.of(topLevel);
        var files = topLevel.listFiles();
        if (topLevel.isDirectory() && files != null) {
            return Stream.concat(
                    Arrays.stream(files).flatMap(this::allFilesToDeleteIn),
                    thisFile);
        }
        return thisFile;
    }
}
