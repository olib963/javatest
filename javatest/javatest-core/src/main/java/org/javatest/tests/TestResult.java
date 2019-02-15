package org.javatest.tests;

import org.javatest.AssertionResult;

public class TestResult {
    public final AssertionResult result;
    public final String testLog;
    public TestResult(AssertionResult result, String testLog) {
        this.result = result;
        this.testLog = testLog;
    }

}
