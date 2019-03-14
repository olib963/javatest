package org.javatest.eventually;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.stream.Stream;

public class EventuallyTests {

    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestProvider, Eventually {
        @Override
        public Stream<Test> testStream() {
            return Stream.of();
        }
    }

    static class FailingTests implements TestProvider, Eventually {
        @Override
        public Stream<Test> testStream() {
            return Stream.of();
        }
    }
}
