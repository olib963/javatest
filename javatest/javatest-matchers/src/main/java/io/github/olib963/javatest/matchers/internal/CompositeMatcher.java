package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

public class CompositeMatcher<A> implements Matcher<A> {
    // TODO do we instead want a NonEmptyList of matchers?
    private final Matcher<A> matcher1;
    private final Matcher<A> matcher2;

    public CompositeMatcher(Matcher<A> matcher1, Matcher<A> matcher2) {
        this.matcher1 = matcher1;
        this.matcher2 = matcher2;
    }

    @Override
    public MatchResult matches(A value) {
        // TODO perhaps we should bundle the expected String into the match result such that it is easier to interject mismatches.
        var firstMatch = matcher1.matches(value);
        var secondMatch = matcher2.matches(value);
        if (firstMatch.matches && secondMatch.matches) {
            return MatchResult.match();
        }
        var firstMisMatch = mismatchString(matcher1, firstMatch);
        var secondMisMatch = mismatchString(matcher2, secondMatch);
        return MatchResult.mismatch(bracket(firstMisMatch) + " and " + bracket(secondMisMatch));
    }

    private String bracket(String message) {
        return '(' + message + ')';
    }

    private String mismatchString(Matcher<A> matcher, MatchResult result) {
        return result.mismatch.orElseGet(() -> {
                    if (result.matches) {
                        return "succeeded to " + matcher.describeExpected();
                    }
                    return "failed to " + matcher.describeExpected();
                });
    }

    @Override
    public String describeExpected() {
        return matcher1.describeExpected() + " and " + matcher2.describeExpected();
    }
}
