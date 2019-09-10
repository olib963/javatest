package io.github.olib963.javatest.fixtures.documentation;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;

// Just used for documentation
public class MySecondUnitTestSuite implements TestSuiteClass {
    @Override
    public Collection<Testable> testables() {
        return List.of();
    }
}
