package io.github.olib963.javatest.parameterised;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = run(testableRunner(new ParameterisedTests()));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}
