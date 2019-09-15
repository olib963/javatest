package io.github.olib963.javatest.reflection.internal;

import java.io.File;
import java.util.stream.Stream;

public class Package {
    public final String packageName;
    public final File directory;
    public Package(String packageName, File directory) {
        this.packageName = packageName;
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "Package{" +
                "packageName='" + packageName + '\'' +
                ", directory=" + directory +
                '}';
    }

    public static Stream<String> classes(Package Package) {
        return Utils.allFilesIn(Package.directory)
                .filter(f -> f.isFile() && f.getName().endsWith(".java"))
                .map(f -> Package.packageName + dropLast5Chars(f.getName()));
    }

    private static String dropLast5Chars(String string) {
        return string.substring(0, string.length() - 5);
    }

}
