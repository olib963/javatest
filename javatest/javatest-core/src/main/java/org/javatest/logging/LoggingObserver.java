package org.javatest.logging;

import org.javatest.TestCompletionObserver;
import org.javatest.TestResult;
import org.javatest.TestResults;

public class LoggingObserver implements TestCompletionObserver {

    public static String SEPARATOR = System.lineSeparator();
    private final boolean useColour;

    public LoggingObserver(boolean useColour) {
        this.useColour = useColour;
    }

    @Override
    public void onTestCompletion(TestResult result) {
        // TODO redefine logs to be structured, only the logger should need to know about line separation and tabbing
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
