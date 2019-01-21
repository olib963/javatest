package org.javatest.matchers;

public interface ExceptionMatchers {

    // TODO describe mismatch
    default Matcher<Runnable> willThrowExceptionThat(Matcher<Exception> exceptionMatcher) {
        return new PredicateMatcher<>(runnable -> {
            try {
                runnable.run();
                return false;
            } catch (Exception e) {
                return exceptionMatcher.matches(e).matches; // TODO compose results. Probably needs to be a new class
            }
        }, "throw an exception that was expected to " + exceptionMatcher.matches(null).expected);
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
