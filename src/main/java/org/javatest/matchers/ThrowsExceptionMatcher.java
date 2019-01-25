package org.javatest.matchers;

public class ThrowsExceptionMatcher implements Matcher<Runnable> {
    private final Matcher<Throwable> thrownMatcher;

    ThrowsExceptionMatcher(Matcher<Throwable> thrownMatcher) {
        this.thrownMatcher = thrownMatcher;
    }

    @Override
    public MatchResult matches(Runnable value) {
        try {
            value.run();
            return MatchResult.mismatch("but no exception was thrown");
        } catch (Exception e) {
            return thrownMatcher.matches(e);
        }
    }

    @Override
    public String describeExpected() {
        return "throw an exception that was expected to " + thrownMatcher.describeExpected();
    }
}
