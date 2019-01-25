package org.javatest.tests;

import org.javatest.logging.TestLog;

public class TestResults {
    public final boolean succeeded;
    public final TestLog testLog;
    public TestResults(boolean succeeded, TestLog testLog) {
        this.succeeded = succeeded;
        this.testLog = testLog;
    }
    public static TestResults init() {
        return new TestResults(true, TestLog.init());
    }

    public TestResults addResult(TestResult result) {
        return new TestResults(succeeded && result.succeeded, testLog.add(result.testLog));
    }

    public TestResults combine(TestResults results) {
        return new TestResults(succeeded && results.succeeded, testLog.addAll(results.testLog));
    }

    @Override
    public String toString() {
        return "TestResults{" +
                "succeeded=" + succeeded +
                ", testLog=" + testLog +
                '}';
    }
}
