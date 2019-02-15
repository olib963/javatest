package org.javatest.matchers;

import java.util.Optional;
import java.util.function.Function;

public class MatchResult {
    public final boolean matches;
    public final Optional<String> mismatch;
    private MatchResult(boolean matches, Optional<String> mismatch) {
        this.matches = matches;
        this.mismatch = mismatch;
    }

    public static MatchResult of(boolean matches, Optional<String> mismatch) {
        return new MatchResult(matches, mismatch);
    }

    public static MatchResult match() {
        return new MatchResult(true, Optional.empty());
    }

    public static MatchResult from(boolean condition) {
        if(condition) {
            return match();
        } else {
            return mismatch();
        }
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
