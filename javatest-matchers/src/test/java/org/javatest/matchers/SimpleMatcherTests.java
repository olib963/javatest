package org.javatest.matchers;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.stream.Stream;

public class SimpleMatcherTests {
    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements MatcherTestProvider {

        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
                    test("Object is the correct type", () -> that("Hello", hasType(String.class)))
            );
        }
    }
    static class FailingTests implements MatcherTestProvider {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                    test("Object is the incorrect type (FAIL)", () -> that("Hello", hasType(int.class)))
            );
        }
    }
}
