package org.javatest.tests;

import org.javatest.logging.TestLogEntry;

public class TestResult {
    public final boolean succeeded;
    public final TestLogEntry testLog;
    public TestResult(boolean succeeded, TestLogEntry testLog) {
        this.succeeded = succeeded;
        this.testLog = testLog;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "succeeded=" + succeeded +
                ", testLog=" + testLog +
                '}';
    }
}
