package io.github.olib963.javatest;

import java.util.Collection;

// Interface used to run the maven plugin
public interface TestRunners {
    Collection<TestRunner> runners();
}
