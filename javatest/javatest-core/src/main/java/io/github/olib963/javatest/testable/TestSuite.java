package io.github.olib963.javatest.testable;

import io.github.olib963.javatest.Testable;

import java.util.stream.Stream;

public class TestSuite implements Testable {
    public final String name;
    public final Stream<Testable> testables;
    public TestSuite(String name, Stream<Testable> testables) {
        this.name = name;
        this.testables = testables;
    }
}
