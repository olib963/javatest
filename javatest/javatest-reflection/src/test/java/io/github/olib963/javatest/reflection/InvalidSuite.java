package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.test;

public class InvalidSuite implements TestSuiteClass {
    private final String notZeroArgConstructor;

    public InvalidSuite(String notZeroArgConstructor) {
        this.notZeroArgConstructor = notZeroArgConstructor;
    }

    @Override
    public Collection<Testable> testables() {
        return List.of(
                test("Test that passes that should never be called", () -> {
                    throw new RuntimeException("This should never be called");
                })
        );
    }
}
