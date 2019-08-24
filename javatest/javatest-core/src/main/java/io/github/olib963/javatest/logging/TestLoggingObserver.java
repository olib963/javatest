package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;

public class TestLoggingObserver implements TestCompletionObserver {

    public static final TestCompletionObserver COLOUR = new TestLoggingObserver(true);
    public static final TestCompletionObserver PLAIN = new TestLoggingObserver(false);

    private final boolean useColour;

    public TestLoggingObserver(boolean useColour) {
        this.useColour = useColour;
    }

    @Override
    public void onTestCompletion(TestResult result) {
        if (useColour) {
            System.out.println(Colour.forResult(result.result).getCode() + result.testLog + Colour.RESET_CODE);
        } else {
            System.out.println(result.testLog);
        }
    }

}
