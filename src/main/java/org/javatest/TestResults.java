package org.javatest;

import org.javatest.logging.TestLog;

public class TestResults {
    public final boolean succeeded;
    public final TestLog testLog;
    public TestResults(boolean succeeded, TestLog testLog) {
        this.succeeded = succeeded;
        this.testLog = testLog;
    }
}
