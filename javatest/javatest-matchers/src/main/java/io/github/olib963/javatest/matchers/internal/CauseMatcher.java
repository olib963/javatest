package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

public class CauseMatcher implements Matcher<Throwable> {
    private final Matcher<Throwable> causeMatcher;

    public CauseMatcher(Matcher<Throwable> causeMatcher) {
        this.causeMatcher = causeMatcher;
    }

    @Override
    public MatchResult matches(Throwable value) {
        var cause = value.getCause();
        return causeMatcher.matches(cause)
                .mapMismatch(mismatch -> "cause {" + Matcher.stringify(cause) + "} " + mismatch);
    }

    @Override
    public String describeExpected() {
        return "have a cause that was expected to " + causeMatcher.describeExpected();
    }
}
