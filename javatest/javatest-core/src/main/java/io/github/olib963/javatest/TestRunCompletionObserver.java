package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.RunLoggingObserver;

@FunctionalInterface
public interface TestRunCompletionObserver {

    void onRunCompletion(TestResults results);

    static TestRunCompletionObserver logger() {
        return RunLoggingObserver.INSTANCE;
    }

}
