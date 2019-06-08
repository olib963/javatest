package io.github.olib963.javatest.benchmark;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = runSuite(new BenchmarkingTests());
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}
