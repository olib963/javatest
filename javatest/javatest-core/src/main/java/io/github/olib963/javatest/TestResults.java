package io.github.olib963.javatest;

import java.util.ArrayList;
import java.util.List;

public final class TestResults {
    public final boolean succeeded;
    public final long successCount;
    public final long failureCount;
    public final long pendingCount;
    private final List<String> testLogs;

    private TestResults(boolean succeeded, long successCount, long failureCount, long pendingCount, List<String> testLogs) {
        this.succeeded = succeeded;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.pendingCount = pendingCount;
        this.testLogs = testLogs;
    }

    public static TestResults init() {
        return new TestResults(true, 0, 0, 0, new ArrayList<>());
    }

    public static TestResults from(long failures, long successes) {
        return new TestResults(failures == 0, successes, failures, 0, new ArrayList<>());
    }

    public TestResults addResult(TestResult result) {
        var logs = new ArrayList<>(testLogs);
        logs.add(result.testLog);
        var assertionResult = result.result;
        if (assertionResult.pending) {
            return new TestResults(succeeded, successCount, failureCount, pendingCount + 1, logs);
        } else if (assertionResult.holds) {
            return new TestResults(succeeded, successCount + 1, failureCount, pendingCount, logs);
        }
        return new TestResults(false, successCount, failureCount + 1, pendingCount, logs);
    }

    public TestResults combine(TestResults results) {
        var logs = new ArrayList<>(testLogs);
        logs.addAll(results.testLogs);
        return new TestResults(
                succeeded && results.succeeded,
                successCount + results.successCount,
                failureCount + results.failureCount,
                pendingCount + results.pendingCount,
                logs);
    }

    public long testCount() {
        return successCount + failureCount + pendingCount;
    }

    public List<String> allLogs() {
        return new ArrayList<>(testLogs);
    }

    public TestResults addLog(String log) {
        var logs = new ArrayList<>(testLogs);
        logs.add(log); // TODO enforce immutability
        return new TestResults(succeeded, successCount, failureCount, pendingCount, logs);
    }

}
