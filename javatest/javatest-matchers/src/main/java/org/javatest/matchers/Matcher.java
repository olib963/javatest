package org.javatest.matchers;

public interface Matcher<A> {
    MatchResult matches(A value);
    String describeExpected();
}
