package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;

import java.io.PrintStream;
import java.util.Optional;
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
            logMessage.logs.forEach(stream::println);
            logMessage.colour.ifPresent(c -> stream.print(Colour.RESET_CODE));
        });
    }

    private Stream<LogMessage> toLogMessages(TestResult result) {
        return result.match(
                suiteResult -> Stream.concat(
                        Stream.of(new LogMessage(Stream.of(suiteResult.suiteName + ':'))),
                        suiteResult.results().flatMap(this::toLogMessages).map(LogMessage::indent)
                    ),
                singleTestResult -> {
                    var logs = Stream.concat(
                            Stream.of(singleTestResult.name + ':'),
                            Stream.concat(
                                    LogMessage.indent(Stream.of(singleTestResult.result.description)),
                                    LogMessage.indent(singleTestResult.logs())));
                    if (useColour) {
                       return Stream.of(new LogMessage(logs, Colour.forResult(singleTestResult.result)));
                    } else {
                        return Stream.of(new LogMessage(logs));
                    }
                }
        );
    }

    private static class LogMessage {
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
            return new LogMessage(indent(logs), colour);
        }

        private static Stream<String> indent(Stream<String> logs) {
            return logs.map(s -> "\t" + s);
        }
    }

}
