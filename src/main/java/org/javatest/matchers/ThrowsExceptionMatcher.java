package org.javatest.matchers;

public class ThrowsExceptionMatcher implements Matcher<Runnable> {
    private final Matcher<Throwable> thrownMatcher;
    private final String expectedPrefix = "throw an exception that was expected to ";

    ThrowsExceptionMatcher(Matcher<Throwable> thrownMatcher) {
        this.thrownMatcher = thrownMatcher;
    }

    @Override
    public MatchResult matches(Runnable value) {
        try {
            value.run();
            return MatchResult.mismatch("throw an exception but none was thrown"); // TODO extract the expected from the matcher so it can be used here?
        } catch (Exception e) {
            var caughtMatch = thrownMatcher.matches(e);
            var expected = expectedPrefix + caughtMatch.expected;
            return caughtMatch.matches ? MatchResult.match(expected): MatchResult.mismatch(expected); // TODO need to show mismatch here.
        }
    }
}
