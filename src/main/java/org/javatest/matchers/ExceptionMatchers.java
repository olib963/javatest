package org.javatest.matchers;

public interface ExceptionMatchers {

    default Matcher<Runnable> willThrowExceptionThat(Matcher<Exception> exceptionMatcher) {
        return new ThrowsExceptionMatcher(exceptionMatcher);
    }

    default  Matcher<Exception> hasMessage(String message) {
        return hasMessageThat(Matcher.isEqualTo(message));
    }

    default  Matcher<Exception> hasMessageThat(Matcher<String> messageMatcher) {
        return new MessageMatcher(messageMatcher);
    }

//    that(x, hasCause(y))
//    that(x, willThrowExceptionThat(hasCause(y)))
//
//    that(x, hasCauseThat(hasMessage(y)))
//    that(x, willThrowExceptionThat(hasCauseThat(hasMessage(y)))
//
    // TODO composable matchers as well as assertions?
//    that(x, willThrowExceptionThat(hasType(y).and(hasCauseThat(hasMessageThat(contains("foo").and(startsWith("bar")))))))
}

class MessageMatcher implements Matcher<Exception> {
    private final Matcher<String> messageMatcher;
    private final String expectedPrefix = "have a message that was expected to ";

    MessageMatcher(Matcher<String> messageMatcher) {
        this.messageMatcher = messageMatcher;
    }

    // TODO extract expected from match result, have mismatch string as part of the result.
    @Override
    public MatchResult matches(Exception value) {
        var result = messageMatcher.matches(value.getMessage());
        var expected = expectedPrefix + result.expected;
        return result.matches? MatchResult.match(expected) : MatchResult.mismatch(expected);
    }
}
