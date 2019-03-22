package org.javatest.parameterised;

import org.javatest.JavaTest;

public class Tests {

    public static void main(String... args) {
        // TODO larger tuple tests.
        // TODO Tuple10
        var result = JavaTest.run(new ParameterisedTests());
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}
