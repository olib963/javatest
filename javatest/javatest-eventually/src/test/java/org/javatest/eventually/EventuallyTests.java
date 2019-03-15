package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.Test;
import org.javatest.TestProvider;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EventuallyTests {

    public static TestProvider passing() {
        return new PassingTests();
    }
    public static TestProvider failing() {
        return new FailingTests();
    }
    // TODO check counts
    // TODO fail test if attempts is < 1

    static class PassingTests implements TestProvider, Eventually {
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
                Supplier<Assertion> valueBecomes6 = () -> {
                    int value = integer.getAndIncrement();
                    return that(value == 6, "Atomic integer (" + value + ") is 6");
                };
                return eventually(valueBecomes6, Duration.ofMillis(50));
            });
        }
    }

    static class FailingTests implements TestProvider, Eventually {

        @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple fail", () -> eventually(() -> that(false, "should fail"), 1)),
                    test("Fails if 0 attempts", () -> eventually(() -> that(true, "should pass"), 0)),
                    test("Fails if negative attempts", () -> eventually(() -> that(true, "should pass"), -1)),
                    atomicIntegerTest()
            );
        }

        private Test atomicIntegerTest() {
            return test("Atomic integer increment", () -> {
                var integer = new AtomicInteger(1);
                Supplier<Assertion> valueBecomes6 = () -> {
                    int value = integer.getAndIncrement();
                    return that(value == 6, "Atomic integer (" + value + ") is 6");
                };
                return eventually(valueBecomes6, Duration.ofMillis(50), 5);
            });
        }
    }
}
