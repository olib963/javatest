package org.javatest.matchers;

public class ThrowsExceptionMatcher implements Matcher<CheckedRunnable> {
    private final Matcher<Throwable> thrownMatcher;

    ThrowsExceptionMatcher(Matcher<Throwable> thrownMatcher) {
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
