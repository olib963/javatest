package io.github.olib963.javatest.fixtures.documentation;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

// Just used for documentation
public class MyIntegrationTestSuite implements TestSuiteClass {

    public MyIntegrationTestSuite(ExecutorService executorService) {}

    @Override
    public Stream<Testable> testables() {
        return Stream.of();
    }
}
