package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.LoggingObserver;

public interface TestCompletionObserver {

    void onTestCompletion(TestResult result);

    void onRunCompletion(TestResults results);

    static TestCompletionObserver plainLogger() {
        return new LoggingObserver(false);
    }

    static TestCompletionObserver colourLogger() {
        return new LoggingObserver(true);
    }
}
