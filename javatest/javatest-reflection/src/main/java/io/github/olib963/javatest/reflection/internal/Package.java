package io.github.olib963.javatest.reflection.internal;

import java.io.File;
import java.util.stream.Stream;

public class Package {
    private final String packageName;
    private final File directory;
    private Package(String packageName, File directory) {
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

    public static Stream<Package> allPackagesUnderSource(File file) {
        return Utils.allFilesIn(file).flatMap(f -> allPackages(f, ""));
    }

    private static Stream<Package> allPackages(File file, String currentPackage) {
        if (file.isDirectory()) {
            var files = Utils.allFilesIn(file);
            var packageName = currentPackage + file.getName() + '.';
            return Stream.concat(
                    Stream.of(new Package(packageName, file)),
                    files.flatMap(f -> allPackages(f, packageName))
            );
        }
        return Stream.empty();
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
