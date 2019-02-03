package org.javatest.tests;

import org.javatest.JavaTest;

import java.util.ArrayList;
import java.util.List;

public class TestResults {
    public final boolean succeeded;
    private final int successCount;
    private final int failureCount;
    private final int pendingCount;
    public final List<String> testLogs;
    private TestResults(boolean succeeded, int successCount, int failureCount, int pendingCount, List<String> testLogs) {
        this.succeeded = succeeded;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.pendingCount = pendingCount;
        this.testLogs = testLogs;
    }
    public static TestResults init() {
        return new TestResults(true, 0,0,0, new ArrayList<>());
    }

    public TestResults addResult(TestResult result) {
        testLogs.add(result.testLog); // TODO enforce immutability
        var assertionResult = result.result;
        if(assertionResult.pending){
            return new TestResults(succeeded, successCount, failureCount, pendingCount + 1, testLogs);
        } else if(assertionResult.holds) {
            return new TestResults(succeeded, successCount + 1, failureCount, pendingCount, testLogs);
        }
        return new TestResults(false, successCount, failureCount + 1, pendingCount, testLogs);
    }

    public TestResults combine(TestResults results) {
        testLogs.addAll(results.testLogs);
        return new TestResults(
                succeeded && results.succeeded,
                successCount + results.successCount,
                failureCount + results.failureCount,
                pendingCount + results.pendingCount,
                testLogs);
    }

    public String totalsLog() {
        var total = successCount + failureCount + pendingCount;
        return "Ran a total of " + total + " tests." + JavaTest.SEPARATOR
                + successCount + " succeeded" + JavaTest.SEPARATOR
                + failureCount + " failed" + JavaTest.SEPARATOR
                + pendingCount + " were pending" + JavaTest.SEPARATOR;
    }

}
