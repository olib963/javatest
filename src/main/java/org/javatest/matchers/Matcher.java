package org.javatest.matchers;

public interface Matcher<A> {
    boolean matches(A value);

    static <A> Matcher<A> isEqualTo(A expected) {
        return new PredicateMatcher<>(expected::equals);
    }

    // TODO break this to accept multiple exception matchers
    static Matcher<Runnable> willThrow(Class<? extends Exception> exceptionClass) {
        return new PredicateMatcher<>(runnable -> {
            try {
                runnable.run();
                return false;
            } catch (Exception e) {
                return exceptionClass.isInstance(e);
            }
        });
    }
}
