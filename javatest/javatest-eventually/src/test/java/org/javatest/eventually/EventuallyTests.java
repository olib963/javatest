package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.CheckedSupplier;
import org.javatest.Test;
import org.javatest.TestSuite;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;
import static org.javatest.eventually.Eventually.*;
import static org.javatest.eventually.EventualConfig.DEFAULT_CONFIG;

public class EventuallyTests {

    public static TestSuite passing() {
        return new PassingTests();
    }
    public static TestSuite failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuite {
        @Override
        public Stream<Test> testStream() {
            var simpleTests = Stream.of(
                    test("Simple pass", () -> eventually(() -> that(true, "should pass"))),
                    test("Eventually pending", () -> eventually(() -> pending("have not yet written eventual condition")))
            );
            // Run the atomic integer 10 times. Repeat might be a good use case for extraction to common function.
            var atomicIntTests = Stream.generate(this::atomicIntegerTest).limit(10);
            return Stream.concat(simpleTests, atomicIntTests);
        }

        private Test atomicIntegerTest() {
            return test("Atomic integer increment", () -> {
                var integer = new AtomicInteger(1);
                CheckedSupplier<Assertion> valueBecomes6 = () -> {
                    int value = integer.getAndIncrement();
                    return that(value == 6, "Atomic integer (" + value + ") is 6");
                };
                return eventually(valueBecomes6, DEFAULT_CONFIG.withWaitInterval(Duration.ofMillis(50)));
            });
        }
    }

    static class FailingTests implements TestSuite {

        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple fail", () -> eventually(() -> that(false, "should fail"), DEFAULT_CONFIG.withAttempts(1))),
                    test("Exception fail", () -> eventually(() -> { throw new Exception("Test failure"); }, DEFAULT_CONFIG.withAttempts(1))),
                    test("Fails if 0 attempts", () -> eventually(() -> that(true, "should pass"), DEFAULT_CONFIG.withAttempts(0))),
                    test("Fails if negative attempts", () -> eventually(() -> that(true, "should pass"), DEFAULT_CONFIG.withAttempts(-1))),
                    test("Fails if empty duration", () -> eventually(() -> that(true, "should pass"), DEFAULT_CONFIG.withWaitInterval(Duration.ZERO))),
                    test("Fails if empty initial delay", () -> eventually(() -> that(true, "should pass"), DEFAULT_CONFIG.withInitialDelay(Duration.ZERO))),
                    atomicIntegerTest()
            );
        }

        private Test atomicIntegerTest() {
            return test("Atomic integer increment", () -> {
                var integer = new AtomicInteger(1);
                CheckedSupplier<Assertion> valueBecomes6 = () -> {
                    int value = integer.getAndIncrement();
                    return that(value == 6, "Atomic integer (" + value + ") is 6");
                };
                return eventually(valueBecomes6, EventualConfig.of(5, Duration.ofMillis(50)));
            });
        }
    }
}
