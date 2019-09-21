package io.github.olib963.javatest;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class TestResult {
    private TestResult() {
    }

    public abstract <A> A match(Function<SuiteResult, A> suiteFn, Function<SingleTestResult, A> singleFn);

    public static class SuiteResult extends TestResult {
        public final String suiteName;
        private final Collection<TestResult> results;
        public SuiteResult(String suiteName, Collection<TestResult> results) {
            this.suiteName = suiteName;
            this.results = results;
        }

        // Java streams are mutable and use once, we need to regenerate a stream on each invocation.
        public Stream<TestResult> results() {
            return results.stream();
        }

        @Override
        public <A> A match(Function<SuiteResult, A> suiteFn, Function<SingleTestResult, A> singleFn) {
            return suiteFn.apply(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SuiteResult that = (SuiteResult) o;
            return suiteName.equals(that.suiteName) &&
                    results.equals(that.results);
        }

        @Override
        public int hashCode() {
            return Objects.hash(suiteName, results);
        }

        @Override
        public String toString() {
            return "SuiteResult{" +
                    "suiteName='" + suiteName + '\'' +
                    ", results=" + results +
                    '}';
        }
    }

    public static class SingleTestResult extends TestResult {
        public final String name;
        public final AssertionResult result;
        private final Collection<String> testLogs;
        public SingleTestResult(String name, AssertionResult result, Collection<String> testLogs) {
            this.name = name;
            this.result = result;
            this.testLogs = testLogs;
        }

        // Java streams are mutable and use once, we need to regenerate a stream on each invocation.
        public Stream<String> logs() {
            return testLogs.stream();
        }

        @Override
        public <A> A match(Function<SuiteResult, A> suiteFn, Function<SingleTestResult, A> singleFn) {
            return singleFn.apply(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SingleTestResult that = (SingleTestResult) o;
            return name.equals(that.name) &&
                    result.equals(that.result) &&
                    testLogs.equals(that.testLogs);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, result, testLogs);
        }

        @Override
        public String toString() {
            return "SingleTestResult{" +
                    "name='" + name + '\'' +
                    ", result=" + result +
                    ", testLogs=" + testLogs +
                    '}';
        }
    }
}
