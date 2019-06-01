package io.github.olib963.javatest;

import java.util.stream.Stream;

// Interface used to run the maven plugin
public interface TestRunners {
    Stream<TestRunner> runners();
}
