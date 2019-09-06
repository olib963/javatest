package io.github.olib963.javatest;

import io.github.olib963.javatest.logging.Colour;
import io.github.olib963.javatest.logging.RunLoggingObserver;
import io.github.olib963.javatest.logging.TestLoggingObserver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

public class LoggingTests implements TestSuiteClass {

    private final TestResult PENDING_TEST_RESULT = new TestResult.SingleTestResult("Pending", AssertionResult.pending(""), Collections.emptyList());

    @Override
    public Stream<Testable> testables() {
        return Stream.of(
                test("Run logging observer", () -> {
                    var stream = new TestStream();

                    var results = TestResults.from(10, 20)
                            .addResult(PENDING_TEST_RESULT)
                            .addResult(PENDING_TEST_RESULT)
                            .addLog("A run level log");

                    var expectedLog = "Ran a total of 32 tests.\\n20 succeeded\\n10 failed\\n2 were pending\\n\\nA run level log\\n";

                    var logger = new RunLoggingObserver(stream.printStream());
                    logger.onRunCompletion(results);

                    var actualLog = escapeSpecial(stream.string());
                    var message = String.format("Expected log message to be {%s} and it was {%s}", expectedLog, actualLog);
                    return that(actualLog.equals(expectedLog), message);
                }),
                test("Test logging observer", () -> {
                    var stream = new TestStream();
                    var testLog = "My Test has completed!";
                    var expectedLog = "My Test:\\n\\t" + testLog + "\\n";
                    var result = new TestResult.SingleTestResult("My Test", AssertionResult.success(""), List.of(testLog));

                    var logger = new TestLoggingObserver(false, stream.printStream());
                    logger.onTestCompletion(result);

                    var actualLog = escapeSpecial(stream.string());
                    var message = String.format("Expected log message to be {%s} and it was {%s}", expectedLog, actualLog);
                    return that(actualLog.equals(expectedLog), message);
                }),
                test("Test logging observer (with colour)", () -> {
                    var stream = new TestStream();
                    var testLog = "My Test has completed!";
                    var expectedLog = ESCAPED_GREEN_CODE + "My Test:\\n\\t" + testLog + "\\n" + ESCAPED_RESET_CODE;
                    var result = new TestResult.SingleTestResult("My Test", AssertionResult.success(""), List.of(testLog));

                    var logger = new TestLoggingObserver(true, stream.printStream());
                    logger.onTestCompletion(result);

                    var actualLog = escapeSpecial(stream.string());
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

    private static final String ESCAPED_RESET_CODE = "\\u001B[0m";
    private static final String ESCAPED_GREEN_CODE = "\\u001B[32m";

    private String escapeSpecial(String input) {
        return input
                .replace(Colour.RESET_CODE, ESCAPED_RESET_CODE)
                .replace(Colour.GREEN.getCode(), ESCAPED_GREEN_CODE)
                .replace("\t", "\\t")
                .replace("\n", "\\n");
    }

}
