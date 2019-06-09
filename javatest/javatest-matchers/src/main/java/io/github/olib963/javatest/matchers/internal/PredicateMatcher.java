package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.MatchResult;
import io.github.olib963.javatest.matchers.Matcher;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateMatcher<A> implements Matcher<A> {
    private final Predicate<A> predicate;
    private final String expected;
    private final Function<A, Optional<String>> mismatchDescriptionFunction;

    private PredicateMatcher(
            Predicate<A> predicate,
            String expected,
            Function<A, Optional<String>> mismatchDescriptionFunction) {
        this.predicate = predicate;
        this.expected = expected;
        this.mismatchDescriptionFunction = mismatchDescriptionFunction;
    }

    public static <A> Matcher<A> of(Predicate<A> predicate, String expected) {
        return new PredicateMatcher<>(predicate, expected, a -> Optional.empty());
    }

    public static <A> Matcher<A> of(Predicate<A> predicate, String expected, Function<A, String> mismatchDescriptionFunction) {
        return new PredicateMatcher<>(predicate, expected, mismatchDescriptionFunction.andThen(Optional::of));
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


    public static <A> Matcher<A> isEqualTo(A expected) {
        return of(expected::equals, "be equal to {" + expected + "}");
    }

    public static <A, T> Matcher<A> hasType(Class<T> expectedClass) {
        return of(
                expectedClass::isInstance,
                "be an instance of {" + expectedClass.getName() + "}",
                value -> "was instead of type {" + value.getClass().getName() + "}"
        );
    }

    public static <A> Matcher<A> isTheSameInstanceAs(A instance) {
        return of(o -> instance == o, "be the same in memory reference as {" + instance + "}");
    }
}