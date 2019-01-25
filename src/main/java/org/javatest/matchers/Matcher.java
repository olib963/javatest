package org.javatest.matchers;

import java.util.Optional;
import java.util.function.Function;

public interface Matcher<A> {
    MatchResult matches(A value);
    String describeExpected();

    static <A> Matcher<A> isEqualTo(A expected) {
        return new PredicateMatcher<>(expected::equals, "be equal to {" + expected + "}");
    }

    static <A, T> Matcher<A> hasType(Class<T> expectedClass) {
        return new PredicateMatcher<>(
                expectedClass::isInstance,
                "be an instance of {" + expectedClass.getName() + "}",
                value -> "was instead of type {" + value.getClass().getName() + "}"
        );
    }

    class MatchResult {
        public final boolean matches;
        public final Optional<String> mismatch;
        MatchResult(boolean matches, Optional<String> mismatch) {
            this.matches = matches;
            this.mismatch = mismatch;
        }

        public static MatchResult match() {
            return new MatchResult(true, Optional.empty());
        }

        public static MatchResult mismatch(){
            return new MatchResult(false, Optional.empty());
        }

        public static MatchResult mismatch(String mismatch){
            return new MatchResult(false, Optional.of(mismatch));
        }

        public MatchResult mapMismatch(Function<String, String> mismatchFunction) {
            return new MatchResult(matches, mismatch.map(mismatchFunction));
        }

    }
}
