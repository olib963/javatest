package io.github.olib963.javatest.fixtures.documentation;

import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

// Just used for documentation
public class MyIntegrationTestSuite implements TestSuite {

    public MyIntegrationTestSuite(ExecutorService executorService) {}

    @Override
    public Stream<Test> tests() {
        return Stream.of();
    }
}
