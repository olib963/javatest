package org.javatest.matchers;

import org.javatest.matchers.internal.CauseMatcher;
import org.javatest.matchers.internal.MessageMatcher;
import org.javatest.matchers.internal.PredicateMatcher;
import org.javatest.matchers.internal.ThrowsExceptionMatcher;

public class ExceptionMatchers {

    private ExceptionMatchers() {}

    static Matcher<CheckedRunnable> willThrowExceptionThat(Matcher<Throwable> exceptionMatcher) {
        return new ThrowsExceptionMatcher(exceptionMatcher);
    }

    static Matcher<Throwable> hasMessage(String message) {
        return hasMessageThat(PredicateMatcher.isEqualTo(message));
    }

    static Matcher<Throwable> hasMessageThat(Matcher<String> messageMatcher) {
        return new MessageMatcher(messageMatcher);
    }

    static Matcher<Throwable> hasCause(Throwable cause) {
        return hasCauseThat(PredicateMatcher.isEqualTo(cause));
    }

    static Matcher<Throwable> hasCauseThat(Matcher<Throwable> causeMatcher) {
        return new CauseMatcher(causeMatcher);
    }

    // TODO composable matchers as well as assertions?
    //    e.g. that(x, willThrowExceptionThat(hasType(y).and(hasCauseThat(hasMessageThat(contains("foo").and(startsWith("bar")))))))

}

