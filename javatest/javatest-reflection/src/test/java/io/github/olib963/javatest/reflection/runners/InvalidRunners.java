package io.github.olib963.javatest.reflection.runners;

import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.javafire.TestRunners;

import java.util.Collection;

public class InvalidRunners implements TestRunners {

    private final String notZeroArgConstructor;

    public InvalidRunners(String notZeroArgConstructor) {
        this.notZeroArgConstructor = notZeroArgConstructor;
    }

    @Override
    public Collection<TestRunner> runners() {
        return new Runners().runners();
    }
}
