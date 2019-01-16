package org.javatest;

import java.util.stream.Stream;

public class JavaTest {

    public static boolean run(Stream<Test> tests) {
        return tests.allMatch(s -> s.assertion.holds());
    }
}
