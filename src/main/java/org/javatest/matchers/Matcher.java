package org.javatest.matchers;

public interface Matcher<A> {
    MatchResult matches(A value);

    static <A> Matcher<A> isEqualTo(A expected) {
        return new PredicateMatcher<>(expected::equals, "be equal to {" + expected + "}");
    }

    // TODO break this to accept multiple exception matchers
    // TODO describe mismatch
    static Matcher<Runnable> willThrow(Class<? extends Exception> exceptionClass) {
        return new PredicateMatcher<>(runnable -> {
            try {
                runnable.run();
                return false;
            } catch (Exception e) {
                return exceptionClass.isInstance(e);
            }
        }, "throw an instance of " + exceptionClass.getName());
    }

    class MatchResult {
        public final boolean matches;
        public final String expected;
        public MatchResult(boolean matches, String expected) {
            this.matches = matches;
            this.expected = expected;
        }

        public static MatchResult match(String expected) {
            return new MatchResult(true, expected);
        }

        public static MatchResult mismatch(String expected){
            return new MatchResult(false, expected);
        }

        public static MatchResult mismatch(String expected, String mismatch){
            return new MatchResult(false, expected);
        }

    }
}
