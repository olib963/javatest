package org.javatest.tests;

import org.javatest.assertions.AssertionResult;

public class TestResult {
    public final AssertionResult result;
    public final String testLog;
    public TestResult(AssertionResult result, String testLog) {
        this.result = result;
        this.testLog = testLog;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "result=" + result +
                ", testLog=" + testLog +
                '}';
    }
}
