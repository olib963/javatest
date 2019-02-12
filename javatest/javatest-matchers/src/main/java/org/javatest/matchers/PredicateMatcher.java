package org.javatest.matchers;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

// TODO possibly provide a simple function matcher A -> MatchResult
public class PredicateMatcher<A> implements Matcher<A> {
    private final Predicate<A> predicate;
    private final String expected;
    private final Function<A, Optional<String>> mismatchDescriptionFunction;

    // TODO figure out solution to constructor problem
    public PredicateMatcher(Predicate<A> predicate, String expected) {
        this(predicate, expected, a -> Optional.empty(), null);
    }

    public PredicateMatcher(Predicate<A> predicate, String expected, Function<A, String> mismatchDescriptionFunction) {
        this(predicate, expected, mismatchDescriptionFunction.andThen(Optional::of), null);
    }
    private PredicateMatcher(
            Predicate<A> predicate,
            String expected,
            Function<A, Optional<String>> mismatchDescriptionFunction,
            String internalValueSoThatConstructorsWorkBecauseJavaHasStupidTypeErasure) {
        this.predicate = predicate;
        this.expected = expected;
        this.mismatchDescriptionFunction = mismatchDescriptionFunction;
    }

    @Override
    public MatchResult matches(A value) {
        if(predicate.test(value)) {
            return MatchResult.match();
        } else {
            return mismatchDescriptionFunction.apply(value)
                    .map(MatchResult::mismatch)
                    .orElseGet(MatchResult::mismatch);
        }
    }

    @Override
    public String describeExpected() {
        return expected;
    }
}