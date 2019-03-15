package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.eventually.internal.EventualAssertion;

import java.time.Duration;
import java.util.function.Supplier;

public interface Eventually {

    // Defaults to 13 attempts over 1 minute.
    default Duration defaultDuration() {
        return Duration.ofSeconds(5);
    }

    default int deafultAttempts() {
        return 13;
    }

    default Assertion eventually(Supplier<Assertion> test) {
        return eventually(test, defaultDuration(), deafultAttempts());
    }

    default Assertion eventually(Supplier<Assertion> test, Duration duration) {
        return eventually(test, duration, deafultAttempts());
    }

    default Assertion eventually(Supplier<Assertion> test, int attempts) {
        return eventually(test, defaultDuration(), attempts);
    }

    default Assertion eventually(Supplier<Assertion> test, Duration duration, int attempts) {
        return new EventualAssertion(test, duration, attempts);
    }

}
