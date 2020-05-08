package io.github.olib963.javatest.javafire;

import io.github.olib963.javatest.TestRunner;

import java.util.Collection;

// TODO move to javafire package
// Interface used to run the maven plugin
public interface TestRunners {
    Collection<TestRunner> runners();
}
