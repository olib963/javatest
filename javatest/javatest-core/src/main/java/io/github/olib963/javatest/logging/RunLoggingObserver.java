package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunCompletionObserver;

import java.io.PrintStream;

public class RunLoggingObserver implements TestRunCompletionObserver {

    public static final String SEPARATOR = System.lineSeparator();
    public static final TestRunCompletionObserver INSTANCE = new RunLoggingObserver(System.out);

    private final PrintStream stream;

    public RunLoggingObserver(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void onRunCompletion(TestResults results) {
        stream.println("Ran a total of " + results.testCount() + " testables." + SEPARATOR
                + results.successCount + " succeeded" + SEPARATOR
                + results.failureCount + " failed" + SEPARATOR
                + results.pendingCount + " were pending");
    }
}
