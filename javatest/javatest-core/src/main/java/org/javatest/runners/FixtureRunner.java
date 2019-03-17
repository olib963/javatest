package org.javatest.runners;

import org.javatest.TestResults;
import org.javatest.TestRunner;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FixtureRunner<F> implements TestRunner {

    private final String resourceName;
    private final Supplier<F> creator;
    private final Consumer<F> destroyer;
    private final Function<F, TestRunner> testFunction;

    public FixtureRunner(String resourceName, Supplier<F> creator, Consumer<F> destroyer, Function<F, TestRunner> testFunction) {
        this.resourceName = resourceName;
        this.creator = creator;
        this.destroyer = destroyer;
        this.testFunction = testFunction;
    }

    @Override
    public TestResults run() {
        return TestResults.init();
    }
}
