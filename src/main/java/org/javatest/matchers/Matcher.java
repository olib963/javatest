package org.javatest.matchers;

public interface Matcher<A> {
    MatchResult matches(A value);

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

    class MatchResult {
        public final boolean matches;
        private MatchResult(boolean matches) {
            this.matches = matches;
        }

        // TODO add expected and mismatch errors
        public static MatchResult match(){
            return new MatchResult(true);
        }

        public static MatchResult match(String expected) {
            return new MatchResult(true);
        }

        public static MatchResult mismatch(){
            return new MatchResult(false);
        }

        public static MatchResult mismatch(String expected){
            return new MatchResult(false);
        }

        public static MatchResult mismatch(String expected, String mismatch){
            return new MatchResult(false);
        }

    }
}
