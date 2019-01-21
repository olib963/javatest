package org.javatest.matchers;

import org.javatest.tests.Test;
import org.javatest.tests.TestProvider;

import java.util.List;
import java.util.stream.Stream;

public class ExceptionMatcherTests {
    private static final List<String> tags = List.of("exception-matchers");
    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestProvider, ExceptionMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Exception of correct type is thrown", () -> that(() -> {
                        throw new RuntimeException("whoopsie");
                    }, willThrow(RuntimeException.class)), tags)
            );
        }
    }

    static class FailingTests implements TestProvider, ExceptionMatchers {
        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Exception of wrong type is thrown (FAIL)", () -> that(() -> { throw new RuntimeException("whoopsie"); }, willThrow(IllegalStateException.class)), tags)
            );
        }
    }
}
