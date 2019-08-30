package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.Colour;
import io.github.olib963.javatest.logging.RunLoggingObserver;
import io.github.olib963.javatest.logging.TestLoggingObserver;
import io.github.olib963.javatest.testable.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

public class LoggingTests implements TestSuiteClass {

    private final TestResult PENDING_TEST_RESULT = new TestResult(AssertionResult.pending(""), "");

    @Override
    public Stream<Testable> testables() {
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
                    var testLog = "My Test has completed!";
                    var expectedLog = testLog + "\n";
                    var result = new TestResult(AssertionResult.success(""), testLog);

                    var logger = new TestLoggingObserver(false, stream.printStream());
                    logger.onTestCompletion(result);

                    var actualLog = stream.string();
                    var message = String.format("Expected log message to be {%s} and it was {%s}", expectedLog, actualLog);
                    return that(actualLog.equals(expectedLog), message);
                }),
                test("Test logging observer (with colour)", () -> {
                    var stream = new TestStream();
                    var testLog = "My Test has completed!";
                    var expectedLog = Colour.GREEN.getCode() + testLog + Colour.RESET_CODE +"\n";
                    var result = new TestResult(AssertionResult.success(""), testLog);

                    var logger = new TestLoggingObserver(true, stream.printStream());
                    logger.onTestCompletion(result);

                    var actualLog = stream.string();
                    var message = String.format("Expected log message to be {%s} and it was {%s}", expectedLog, actualLog);
                    return that(actualLog.equals(expectedLog), message);
                }),
                colourTest("Colour for failure", Colour.RED, AssertionResult.failure("")),
                colourTest("Colour for success", Colour.GREEN, AssertionResult.success("")),
                colourTest("Colour for pending", Colour.YELLOW, AssertionResult.pending(""))
        );
    }

    private Test colourTest(String name, Colour colour, AssertionResult result) {
        return test(name, () -> {
            var actualColour = Colour.forResult(result);
            var message = String.format("Expected %s for a result of %s (got %s)", colour, result, actualColour);
            return that(actualColour == colour, message);
        });
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