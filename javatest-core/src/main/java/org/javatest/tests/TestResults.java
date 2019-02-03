package org.javatest.tests;

import java.util.ArrayList;
import java.util.List;

public class TestResults {
    public final boolean succeeded;
    public final List<String> testLogs;
    private TestResults(boolean succeeded, List<String> testLogs) {
        this.succeeded = succeeded;
        this.testLogs = testLogs;
    }
    public static TestResults init() {
        return new TestResults(true, new ArrayList<>());
    }

    public TestResults addResult(TestResult result) {
        testLogs.add(result.testLog); // TODO enforce immutability
        return new TestResults(succeeded && result.succeeded, testLogs);
    }

    public TestResults combine(TestResults results) {
        testLogs.addAll(results.testLogs);
        return new TestResults(succeeded && results.succeeded, testLogs);
    }

    @Override
    public String toString() {
        return "TestResults{" +
                "succeeded=" + succeeded +
                ", testLogs=" + testLogs +
                '}';
    }
}
