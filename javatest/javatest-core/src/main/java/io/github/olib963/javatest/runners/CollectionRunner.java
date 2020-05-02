package io.github.olib963.javatest.runners;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.Testable;

import java.util.Collection;

public class CollectionRunner implements TestRunner {
    private final Collection<? extends Testable> testables;

    public CollectionRunner(Collection<? extends Testable> testables) {
        this.testables = testables;
    }

    @Override
    public TestResults run() {
        return new StreamRunner(testables.stream()).run();
    }
}
