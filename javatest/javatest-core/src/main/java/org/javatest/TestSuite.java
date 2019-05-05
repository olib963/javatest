package org.javatest;

import java.util.stream.Stream;

public interface TestSuite {
    Stream<Test> testStream();
}
