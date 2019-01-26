package org.javatest.matchers;

import org.javatest.JavaTest;

// TODO create maven plugin so this is not needed.
public class FunctionalTest {
    public static void main(String... args) {
        var result = JavaTest.run(new Tests().testStream());

        if(!result.succeeded) {
            throw new TestFailedException("Tests failed!");
        }

        System.out.println("Functional Tests passed");
    }

    static class TestFailedException extends RuntimeException {
        TestFailedException(String message) {
            super(message, null, true, false);
        }
    }

}

