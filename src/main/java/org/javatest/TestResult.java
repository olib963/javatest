package org.javatest;

public class TestResult {
    public final boolean succeeded;
    public final String testLog;
    public TestResult(boolean succeeded, String testLog) {
        this.succeeded = succeeded;
        this.testLog = testLog;
    }
}
