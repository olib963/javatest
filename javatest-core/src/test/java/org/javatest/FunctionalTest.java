package org.javatest;

public class FunctionalTest {
    public static void main(String... args) {
        if(!JavaTest.run(new Tests()).succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Functional Tests passed");
    }

}

