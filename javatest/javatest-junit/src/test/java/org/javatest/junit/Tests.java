package org.javatest.junit;

import org.javatest.JavaTest;

public class Tests {

    public static void main(String... args) {
        // TODO setup some JUnit tests, some that pass and some that fail, test the JUnit runner passed and fails both accordingly
        // TODO expose the counts of run, pass, fail so that we can test the amounts
        var result = JavaTest.run(new JUnitTestRunner());
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }

}
