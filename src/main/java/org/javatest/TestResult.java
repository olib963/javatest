package org.javatest;

import org.javatest.logging.TestLogEntry;

public class TestResult {
    public final boolean succeeded;
    public final TestLogEntry testLog;
    public TestResult(boolean succeeded, TestLogEntry testLog) {
        this.succeeded = succeeded;
        this.testLog = testLog;
    }
}
