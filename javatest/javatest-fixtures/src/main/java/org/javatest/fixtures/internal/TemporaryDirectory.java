package org.javatest.fixtures.internal;

import org.javatest.fixtures.Fixture;

import java.io.File;
import java.util.Arrays;

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
        // TODO change to collect failures
        if (fixture.isDirectory()) {
            var files = fixture.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(this::destroy);
            }
        }
        if (!fixture.delete()) {
            throw new IllegalStateException("Could not delete file " + fixture.getAbsolutePath());
        }
    }
}
