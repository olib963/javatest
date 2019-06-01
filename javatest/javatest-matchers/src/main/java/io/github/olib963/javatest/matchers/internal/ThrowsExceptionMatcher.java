package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.CheckedRunnable;
import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

public class ThrowsExceptionMatcher implements Matcher<CheckedRunnable> {
    private final Matcher<Throwable> thrownMatcher;

    public ThrowsExceptionMatcher(Matcher<Throwable> thrownMatcher) {
        this.thrownMatcher = thrownMatcher;
    }

    @Override
    public MatchResult matches(CheckedRunnable value) {
        try {
            value.run();
            return MatchResult.mismatch("no exception was thrown");
        } catch (Exception e) {
            return thrownMatcher.matches(e);
        }
    }

    @Override
    public String describeExpected() {
        return "throw an exception that was expected to " + thrownMatcher.describeExpected();
    }
}
