package org.javatest.matchers;

public interface ExceptionMatchers {

    default Matcher<Runnable> willThrowExceptionThat(Matcher<Exception> exceptionMatcher) {
        return new ThrowsExceptionMatcher(exceptionMatcher);
    }

//    that(x, hasMessage(y))
//    that(x, willThrowExceptionThat(hasMessage(y)))
//
//    that(x, hasMessageThat(contains(y)))
//    that(x, willThrowExceptionThat(hasMessageThat(contains(y))))
//
//    that(x, hasCause(y))
//    that(x, willThrowExceptionThat(hasCause(y)))
//
//    that(x, hasCauseThat(hasMessage(y)))
//    that(x, willThrowExceptionThat(hasCauseThat(hasMessage(y)))
//
    // TODO composable matchers as well as assertions?
//    that(x, willThrowExceptionThat(hasType(y).and(hasCauseThat(hasMessageThat(contains("foo").and(startsWith("bar")))))))
}
