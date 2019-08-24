package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunCompletionObserver;

public class RunLoggingObserver implements TestRunCompletionObserver {

    public static final String SEPARATOR = System.lineSeparator();
    public static final TestRunCompletionObserver INSTANCE = new RunLoggingObserver();

    @Override
    public void onRunCompletion(TestResults results) {
        System.out.println("Ran a total of " + results.testCount() + " tests." + SEPARATOR
                + results.successCount + " succeeded" + SEPARATOR
                + results.failureCount + " failed" + SEPARATOR
                + results.pendingCount + " were pending" + SEPARATOR);
    }
}
