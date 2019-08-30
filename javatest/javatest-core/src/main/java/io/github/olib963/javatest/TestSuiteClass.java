package io.github.olib963.javatest;

import io.github.olib963.javatest.testable.TestSuite;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.suite;

public interface TestSuiteClass extends Testable {

    default TestSuite toSuite() {
        return suite(getClass().getSimpleName(), testables());
    }

    Stream<Testable> testables();
}
