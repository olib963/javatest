package org.javatest;

public final class TestResult {
    public final AssertionResult result;
    public final String testLog;
    public TestResult(AssertionResult result, String testLog) {
        this.result = result;
        this.testLog = testLog;
    }
}
