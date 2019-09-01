package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;

import java.io.PrintStream;
import java.util.Optional;
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
        toLogMessages(result).forEach(logMessage -> {
            logMessage.colour.ifPresent(c -> stream.print(c.getCode()));
            stream.println(logMessage.logs.collect(Collectors.joining("\n")));
            logMessage.colour.ifPresent(c -> stream.print(Colour.RESET_CODE));
        });
    }

    // TODO change tests to use this function instead.
    Stream<LogMessage> toLogMessages(TestResult result) {
        return result.match(
                suiteResult -> Stream.concat(
                        // TODO colour for suites
                        Stream.of(new LogMessage(Stream.of(suiteResult.suiteName + ':'))),
                        suiteResult.results().flatMap(this::toLogMessages).map(LogMessage::indent)
                    ),
                singleTestResult -> {
                    if (useColour) {
                       return Stream.of(new LogMessage(singleTestResult.logs(), Colour.forResult(singleTestResult.result)));
                    } else {
                        return Stream.of(new LogMessage(singleTestResult.logs()));
                    }
                }
        );
    }

    private class LogMessage {
        final Stream<String> logs;
        final Optional<Colour> colour;

        private LogMessage(Stream<String> logs, Optional<Colour> colour) {
            this.logs = logs;
            this.colour = colour;
        }
        private LogMessage(Stream<String> logs, Colour colour) {
            this(logs, Optional.of(colour));
        }
        private LogMessage(Stream<String> logs) {
            this(logs, Optional.empty());
        }
        private LogMessage indent() {
            return new LogMessage(logs.map(s -> "\t" + s), colour);
        }
    }

}
