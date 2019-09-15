package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;

public class SimpleSuite implements TestSuiteClass {
    @Override
    public Collection<Testable> testables() {
        return List.of(
                test("Pending test", JavaTest::pending),
                test("Passing test", () -> that(true, "passing"))
        );
    }
}
