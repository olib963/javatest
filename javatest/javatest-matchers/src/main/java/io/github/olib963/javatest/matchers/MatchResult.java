package io.github.olib963.javatest.matchers;

import java.util.Optional;
import java.util.function.Function;

public final class MatchResult {
    public final boolean matches;
    public final Optional<String> mismatch;
    private MatchResult(boolean matches, Optional<String> mismatch) {
        this.matches = matches;
        this.mismatch = mismatch;
    }

    public static MatchResult of(boolean matches) {
        return of(matches, Optional.empty());
    }

    public static MatchResult of(boolean matches, Optional<String> mismatch) {
        return new MatchResult(matches, mismatch);
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
