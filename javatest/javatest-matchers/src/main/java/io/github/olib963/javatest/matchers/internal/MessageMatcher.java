package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

// TODO Message, Cause and Throws are all a wrapper around a function and an existing matcher.
public class MessageMatcher implements Matcher<Throwable> {
    private final Matcher<String> messageMatcher;

    public MessageMatcher(Matcher<String> messageMatcher) {
        this.messageMatcher = messageMatcher;
    }

    @Override
    public MatchResult matches(Throwable value) {
        var message = value.getMessage();
        return messageMatcher.matches(message)
                .mapMismatch(mismatch -> "message {" + message + "} " + mismatch);
    }

    @Override
    public String describeExpected() {
        return "have a message that was expected to " + messageMatcher.describeExpected();
    }
}
