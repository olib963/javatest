package io.github.olib963.javatest.fixtures.documentation;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.stream.Stream;

// Just used for documentation
public class MySecondUnitTestSuite implements TestSuiteClass {
    @Override
    public Stream<Testable> testables() {
        return Stream.of();
    }
}
