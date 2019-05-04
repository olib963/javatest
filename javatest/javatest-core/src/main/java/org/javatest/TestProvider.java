package org.javatest;

import java.util.stream.Stream;

// TODO rename to TestSuite
public interface TestProvider {
    Stream<Test> testStream();
}
