package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.TestLoggingObserver;

import java.io.PrintStream;

@FunctionalInterface
public interface TestCompletionObserver {

    void onTestCompletion(TestResult result);

    static TestCompletionObserver plainLogger() {
        return TestLoggingObserver.PLAIN;
    }

    static TestCompletionObserver colourLogger() {
        return TestLoggingObserver.COLOUR;
    }

    static TestCompletionObserver logTo(boolean colour, PrintStream stream) {
        return new TestLoggingObserver(colour, stream);
    }
}
