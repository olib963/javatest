package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;

import java.io.PrintStream;

public class TestLoggingObserver implements TestCompletionObserver {

    public static final TestCompletionObserver COLOUR = new TestLoggingObserver(true, System.out);
    public static final TestCompletionObserver PLAIN = new TestLoggingObserver(false, System.out);

    private final boolean useColour;
    private final PrintStream stream;

    public TestLoggingObserver(boolean useColour, PrintStream stream) {
        this.useColour = useColour;
        this.stream = stream;
    }

    @Override
    public void onTestCompletion(TestResult result) {
        if (useColour) {
            stream.println(Colour.forResult(result.result).getCode() + result.testLog + Colour.RESET_CODE);
        } else {
            stream.println(result.testLog);
        }
    }

}
