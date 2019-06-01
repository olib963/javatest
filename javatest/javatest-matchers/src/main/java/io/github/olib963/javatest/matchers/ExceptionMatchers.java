package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.matchers.internal.CauseMatcher;
import io.github.olib963.javatest.matchers.internal.MessageMatcher;
import io.github.olib963.javatest.matchers.internal.PredicateMatcher;
import io.github.olib963.javatest.matchers.internal.ThrowsExceptionMatcher;

public class ExceptionMatchers {

    private ExceptionMatchers() {}

    public static Matcher<CheckedRunnable> willThrowExceptionThat(Matcher<Throwable> exceptionMatcher) {
        return new ThrowsExceptionMatcher(exceptionMatcher);
    }

    public static Matcher<Throwable> hasMessage(String message) {
        return hasMessageThat(PredicateMatcher.isEqualTo(message));
    }

    public static Matcher<Throwable> hasMessageThat(Matcher<String> messageMatcher) {
        return new MessageMatcher(messageMatcher);
    }

    public static Matcher<Throwable> hasCause(Throwable cause) {
        return hasCauseThat(PredicateMatcher.isEqualTo(cause));
    }

    public static Matcher<Throwable> hasCauseThat(Matcher<Throwable> causeMatcher) {
        return new CauseMatcher(causeMatcher);
    }

    // TODO composable matchers as well as assertions?
    //    e.g. that(x, willThrowExceptionThat(hasType(y).and(hasCauseThat(hasMessageThat(contains("foo").and(startsWith("bar")))))))

}

