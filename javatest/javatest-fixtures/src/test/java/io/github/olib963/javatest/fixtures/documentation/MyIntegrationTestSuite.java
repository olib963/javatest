package io.github.olib963.javatest.fixtures.documentation;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

// Just used for documentation
public class MyIntegrationTestSuite implements TestSuiteClass {

    public MyIntegrationTestSuite(ExecutorService executorService) {}

    @Override
    public Collection<Testable> testables() {
        return List.of();
    }
}
