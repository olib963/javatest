package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.RunLoggingObserver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class LoggingTests implements TestSuite {

    private final TestResult PENDING_TEST_RESULT = new TestResult(AssertionResult.pending(""), "");

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                test("Run logging observer", () -> {
                    var stream = new TestStream();

                    var results = TestResults.from(10, 20)
                            .addResult(PENDING_TEST_RESULT)
                            .addResult(PENDING_TEST_RESULT);
                    var expectedLog = "Ran a total of 32 tests.\n20 succeeded\n10 failed\n2 were pending\n";

                    var logger = new RunLoggingObserver(stream.printStream());
                    logger.onRunCompletion(results);

                    var actualLog = stream.string();
                    var message = String.format("Expected log message to be {%s} and it was {%s}", expectedLog, actualLog);
                    return that(actualLog.equals(expectedLog), message);
                }),
                test("Test logging observer", () -> {
                    var stream = new TestStream();
                    return pending();
                }),
                test("Test logging observer (with colour)", () -> {
                    var stream = new TestStream();
                    return pending();
                }),
                test("Colour for result", JavaTest::pending)

        );
    }

    private class TestStream {
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        private PrintStream printStream(){
            return new PrintStream(baos, true, StandardCharsets.UTF_8);
        }
        private String string() {
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

}
