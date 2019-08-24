package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.TestLoggingObserver;

@FunctionalInterface
public interface TestCompletionObserver {

    void onTestCompletion(TestResult result);

    static TestCompletionObserver plainLogger() {
        return TestLoggingObserver.PLAIN;
    }

    static TestCompletionObserver colourLogger() {
        return TestLoggingObserver.COLOUR;
    }
}
