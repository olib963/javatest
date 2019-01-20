package org.javatest.tests;

import org.javatest.assertions.Assertion;
import org.javatest.matchers.Matcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface TestProvider {

    Stream<Test> testStream();

    default Test test(String description, Supplier<Assertion> test) {
        return Test.test(description, test, Collections.emptyList());
    }

    default Test test(String description, Supplier<Assertion> test, Collection<String> tags) {
        return Test.test(description, test, tags);
    }

    default <A> Assertion that(A value, Matcher<A> matcher) {
        return Assertion.that(value, matcher);
    }

    default Assertion that(boolean asserted) { return Assertion.that(asserted); }

    default Assertion pending() {
        return Assertion.pending();
    }

    default <A> Matcher<A> isEqualTo(A expected) {
        return Matcher.isEqualTo(expected);
    }

    default Matcher<Runnable> willThrow(Class<? extends Exception> exceptionClass) {
        return Matcher.willThrow(exceptionClass);
    }

    default Stream<Test> allTestsFrom(TestProvider... providers) {
        return Arrays.stream(providers).flatMap(TestProvider::testStream);
    }
}
