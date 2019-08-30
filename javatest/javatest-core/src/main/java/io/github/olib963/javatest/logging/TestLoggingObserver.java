package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;

import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        stream.println(toLogMessages(result).collect(Collectors.joining("\n")));
    }

    // TODO change tests to use this function instead.
    Stream<String> toLogMessages(TestResult result) {
        return result.match(
                suiteResult -> Stream.concat(
                        Stream.of(suiteResult.suiteName + ':'),
                        suiteResult.results().flatMap(this::toLogMessages).map(s -> "\t" + s)
                    ),
                singleTestResult -> {
                    if (useColour) {
                       return Stream.of(Colour.forResult(singleTestResult.result).getCode() + singleTestResult.testLog + Colour.RESET_CODE);
                    } else {
                        return Stream.of(singleTestResult.testLog);
                    }
                }
        );
    }

}
