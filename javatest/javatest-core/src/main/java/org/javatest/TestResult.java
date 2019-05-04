package org.javatest;

final public class TestResult {
    public final AssertionResult result;
    public final String testLog;
    public TestResult(AssertionResult result, String testLog) {
        this.result = result;
        this.testLog = testLog;
    }
}
