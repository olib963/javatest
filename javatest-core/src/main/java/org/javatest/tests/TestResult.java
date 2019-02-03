package org.javatest.tests;

public class TestResult {
    public final boolean succeeded;
    public final String testLog;
    public TestResult(boolean succeeded, String testLog) {
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
