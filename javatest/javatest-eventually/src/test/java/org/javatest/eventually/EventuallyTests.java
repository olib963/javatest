package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.CheckedSupplier;
import org.javatest.Test;
import org.javatest.TestSuite;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class EventuallyTests {

    public static TestSuite passing() {
        return new PassingTests();
    }
    public static TestSuite failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuite, Eventually {
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
                return eventually(valueBecomes6, Duration.ofMillis(50));
            });
        }
    }

    static class FailingTests implements TestSuite, Eventually {

        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple fail", () -> eventually(() -> that(false, "should fail"), 1)),
                    test("Exception fail", () -> eventually(() -> { throw new Exception("Test failure"); }, 1)),
                    test("Fails if 0 attempts", () -> eventually(() -> that(true, "should pass"), 0)),
                    test("Fails if negative attempts", () -> eventually(() -> that(true, "should pass"), -1)),
                    test("Fails if empty duration", () -> eventually(() -> that(true, "should pass"), Duration.ZERO)),
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
                return eventually(valueBecomes6, Duration.ofMillis(50), 5);
            });
        }
    }
}
