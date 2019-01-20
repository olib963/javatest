package org.javatest;

public class FunctionalTest {
    public static void main(String... args) {
        var result = JavaTest.run(new Tests().testStream());

        if(!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        System.out.println("Functional Tests passed");
    }

}
