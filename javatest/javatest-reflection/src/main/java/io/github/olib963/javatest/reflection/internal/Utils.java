package io.github.olib963.javatest.reflection.internal;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class Utils {

    public static Stream<File> allFilesIn(File file) {
        var files = file.listFiles();
        if(files == null) {
            return Stream.empty();
        }
        return Arrays.stream(files);
    }

}
