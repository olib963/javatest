package org.javatest.matchers;

public interface ExceptionMatchers {

    // TODO break this to accept multiple exception matchers
    // TODO describe mismatch
    default Matcher<Runnable> willThrow(Class<? extends Exception> exceptionClass) {
        return new PredicateMatcher<>(runnable -> {
            try {
                runnable.run();
                return false;
            } catch (Exception e) {
                return exceptionClass.isInstance(e);
            }
        }, "throw an instance of " + exceptionClass.getName());
    }
}
