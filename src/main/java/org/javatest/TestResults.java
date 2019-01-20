package org.javatest;

public class TestResults {
    public final boolean succeeded;
    public final String testLog;
    public TestResults(boolean succeeded, String testLog) {
        this.succeeded = succeeded;
        this.testLog = testLog;
    }
}
