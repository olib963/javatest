package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunCompletionObserver;

import java.io.PrintStream;
import java.util.stream.Collectors;

public class RunLoggingObserver implements TestRunCompletionObserver {


    public static final TestRunCompletionObserver INSTANCE = new RunLoggingObserver(System.out);

    private final PrintStream stream;

    public RunLoggingObserver(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void onRunCompletion(TestResults results) {
        stream.println("Ran a total of " + results.testCount() + " tests.");
        stream.println(results.successCount + " succeeded");
        stream.println(results.failureCount + " failed");
        if (results.pendingCount == 1) {
            stream.println("1 was pending");
        } else {
            stream.println(results.pendingCount + " were pending");
        }
        if (results.allLogs().count() != 0) {
            stream.println();
        }
        results.allLogs().forEach(stream::println);

    }
}
