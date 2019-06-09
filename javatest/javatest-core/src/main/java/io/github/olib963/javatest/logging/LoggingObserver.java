package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;
import io.github.olib963.javatest.TestResults;

public class LoggingObserver implements TestCompletionObserver {

    public static final String SEPARATOR = System.lineSeparator();
    private final boolean useColour;

    public LoggingObserver(boolean useColour) {
        this.useColour = useColour;
    }

    @Override
    public void onTestCompletion(TestResult result) {
        if (useColour) {
            System.out.println(Colour.forResult(result.result).getCode() + result.testLog + Colour.resetCode());
        } else {
            System.out.println(result.testLog);
        }
    }

    @Override
    public void onRunCompletion(TestResults results) {
        System.out.println("Ran a total of " + results.testCount() + " tests." + SEPARATOR
                + results.successCount + " succeeded" + SEPARATOR
                + results.failureCount + " failed" + SEPARATOR
                + results.pendingCount + " were pending" + SEPARATOR);
    }
}
