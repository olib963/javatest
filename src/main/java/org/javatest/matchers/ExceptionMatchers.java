package org.javatest.matchers;

public interface ExceptionMatchers {

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
    //    e.g. that(x, willThrowExceptionThat(hasType(y).and(hasCauseThat(hasMessageThat(contains("foo").and(startsWith("bar")))))))
}

// TODO Message, Cause and Throws are all a wrapper around a function and an existing matcher.
class MessageMatcher implements Matcher<Throwable> {
    private final Matcher<String> messageMatcher;

    MessageMatcher(Matcher<String> messageMatcher) {
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

class CauseMatcher implements Matcher<Throwable> {
    private final Matcher<Throwable> causeMatcher;

    CauseMatcher(Matcher<Throwable> causeMatcher) {
        this.causeMatcher = causeMatcher;
    }

    @Override
    public MatchResult matches(Throwable value) {
        var cause = value.getCause();
        return causeMatcher.matches(cause)
                .mapMismatch(mismatch -> "cause {" + cause + "} " + mismatch);
    }

    @Override
    public String describeExpected() {
        return "have a cause that was expected to " + causeMatcher.describeExpected();
    }
}
