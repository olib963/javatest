package io.github.olib963.javatest.reflection;

import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.TestRunners;

import java.util.Collection;
import java.util.Collections;

public class ReflectionRunners implements TestRunners {
    @Override
    public Collection<TestRunner> runners() {
        return Collections.emptyList();
    }
}
