package org.javatest.matchers;

public interface ExceptionMatchers {

    // TODO I had to change all to throwable because cause: Throwable not Exception. Is this acceptable?
    default Matcher<Runnable> willThrowExceptionThat(Matcher<Throwable> exceptionMatcher) {
        return new ThrowsExceptionMatcher(exceptionMatcher);
    }

    default Matcher<Throwable> hasMessage(String message) {
        return hasMessageThat(Matcher.isEqualTo(message));
    }

    default Matcher<Throwable> hasMessageThat(Matcher<String> messageMatcher) {
        return new MessageMatcher(messageMatcher);
    }

    default Matcher<Throwable> hasCause(Throwable cause) {
        return hasCauseThat(Matcher.isEqualTo(cause));
    }

    default Matcher<Throwable> hasCauseThat(Matcher<Throwable> causeMatcher) {
        return new CauseMatcher(causeMatcher);
    }

    // TODO composable matchers as well as assertions?
//    that(x, willThrowExceptionThat(hasType(y).and(hasCauseThat(hasMessageThat(contains("foo").and(startsWith("bar")))))))
}

class MessageMatcher implements Matcher<Throwable> {
    private final Matcher<String> messageMatcher;
    private final String expectedPrefix = "have a message that was expected to ";

    MessageMatcher(Matcher<String> messageMatcher) {
        this.messageMatcher = messageMatcher;
    }

    // TODO extract expected from match result, have mismatch string as part of the result.
    @Override
    public MatchResult matches(Throwable value) {
        var result = messageMatcher.matches(value.getMessage());
        var expected = expectedPrefix + result.expected;
        return result.matches? MatchResult.match(expected) : MatchResult.mismatch(expected);
    }
}

class CauseMatcher implements Matcher<Throwable> {
    private final Matcher<Throwable> causeMatcher;
    private final String expectedPrefix = "have a cause that was expected to ";

    CauseMatcher(Matcher<Throwable> causeMatcher) {
        this.causeMatcher = causeMatcher;
    }

    // TODO extract expected from match result, have mismatch string as part of the result.
    @Override
    public MatchResult matches(Throwable value) {
        var result = causeMatcher.matches(value.getCause());
        var expected = expectedPrefix + result.expected;
        return result.matches ? MatchResult.match(expected) : MatchResult.mismatch(expected);
    }
}
