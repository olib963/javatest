package io.github.olib963.javatest;

import java.util.Collection;
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
    }

    public static class SingleTestResult extends TestResult {
        public final AssertionResult result;
        public final String testLog;
        public SingleTestResult(AssertionResult result, String testLog) {
            this.result = result;
            this.testLog = testLog;
        }

        @Override
        public <A> A match(Function<SuiteResult, A> suiteFn, Function<SingleTestResult, A> singleFn) {
            return singleFn.apply(this);
        }
    }
}
