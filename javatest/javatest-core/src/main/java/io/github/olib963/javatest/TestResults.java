package io.github.olib963.javatest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class TestResults {
    public final boolean succeeded;
    public final long successCount;
    public final long failureCount;
    public final long pendingCount;
    private final List<String> runLogs;
    private final List<TestResult> results;

    private TestResults(boolean succeeded,
                        long successCount,
                        long failureCount,
                        long pendingCount,
                        List<String> runLogs,
                        List<TestResult> results) {
        this.succeeded = succeeded;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.pendingCount = pendingCount;
        this.runLogs = runLogs;
        this.results = results;
    }

    public static TestResults empty() {
        return new TestResults(true, 0, 0, 0,
                Collections.emptyList(), Collections.emptyList());
    }

    public static TestResults from(long failures, long successes) {
        return new TestResults(failures == 0, successes, failures, 0,
                Collections.emptyList(), Collections.emptyList());
    }

    public TestResults addResult(TestResult result) {
        var newResults = new ArrayList<>(results);
        newResults.add(result);

        var aggregated = aggregate(result);
        return new TestResults(
                succeeded && aggregated.succeeded,
                successCount + aggregated.successCount,
                failureCount + aggregated.failureCount,
                pendingCount + aggregated.pendingCount,
                runLogs,
                newResults);

    }

    private Aggregate aggregate(TestResult result) {
        return result.match(
                suiteResult -> suiteResult.results()
                        .map(this::aggregate)
                        .reduce(new Aggregate(true, 0, 0, 0), Aggregate::combine),
                testResult -> {
                    var assertionResult = testResult.result;
                    if (assertionResult.pending) {
                        return new Aggregate(true, 0, 0, 1);
                    } else if (assertionResult.holds) {
                        return new Aggregate(true, 1, 0, 0);
                    }
                    return new Aggregate(false, 0,  1, 0);
                }
        );
    }

    // TODO remove duplication
    private class Aggregate{
        private final boolean succeeded;
        private final long successCount;
        private final long failureCount;
        private final long pendingCount;
        private Aggregate(boolean succeeded, long successCount, long failureCount, long pendingCount) {
            this.succeeded = succeeded;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.pendingCount = pendingCount;
        }
        private Aggregate combine(Aggregate other) {
            return new Aggregate(succeeded && other.succeeded,
                    successCount + other.successCount,
                    failureCount + other.failureCount,
                    pendingCount + other.pendingCount);
        }
    }

    public TestResults combine(TestResults results) {
        var logs = new ArrayList<>(runLogs);
        logs.addAll(results.runLogs);
        var allResults = new ArrayList<>(this.results);
        allResults.addAll(results.results);
        return new TestResults(
                succeeded && results.succeeded,
                successCount + results.successCount,
                failureCount + results.failureCount,
                pendingCount + results.pendingCount,
                logs,
                allResults);
    }

    public long testCount() {
        return successCount + failureCount + pendingCount;
    }

    public Stream<String> allLogs() {
        return runLogs.stream();
    }

    public Stream<TestResult> allResults() {
        return results.stream();
    }

    public TestResults addLog(String log) {
        var logs = new ArrayList<>(runLogs);
        logs.add(log);
        return new TestResults(succeeded, successCount, failureCount, pendingCount, logs, results);
    }

    public TestResults failBecause(String reason){
        return new TestResults(false, successCount, failureCount, pendingCount, runLogs, results).addLog(reason);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResults that = (TestResults) o;
        return succeeded == that.succeeded &&
                successCount == that.successCount &&
                failureCount == that.failureCount &&
                pendingCount == that.pendingCount &&
                runLogs.equals(that.runLogs) &&
                results.equals(that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(succeeded, successCount, failureCount, pendingCount, runLogs, results);
    }

    @Override
    public String toString() {
        return "TestResults{" +
                "succeeded=" + succeeded +
                ", successCount=" + successCount +
                ", failureCount=" + failureCount +
                ", pendingCount=" + pendingCount +
                ", runLogs=" + runLogs +
                ", results=" + results +
                '}';
    }
}
