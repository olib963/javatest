package org.javatest.matchers;

import java.util.function.Predicate;

public interface Matcher<A> {
    boolean matches(A value);

    class PredicateMatcher<A> implements Matcher<A> {
        private final Predicate<A> predicate;
        public PredicateMatcher(Predicate<A> predicate) {
            this.predicate = predicate;
        }
        @Override
        public boolean matches(A value) {
            return predicate.test(value);
        }
    }
    static <A> Matcher<A> isEqualTo(A expected) {
        return new PredicateMatcher<>(expected::equals);
    }
}
