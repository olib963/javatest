package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.CheckedSupplier;
import org.javatest.eventually.internal.EventualAssertion;

import java.time.Duration;

public interface Eventually {

    // Defaults to 13 attempts over 1 minute.
    default Duration defaultDuration() {
        return Duration.ofSeconds(5);
    }

    default int defaultAttempts() {
        return 13;
    }

    default Assertion eventually(CheckedSupplier<Assertion> test) {
        return eventually(test, defaultDuration(), defaultAttempts());
    }

    default Assertion eventually(CheckedSupplier<Assertion> test, Duration duration) {
        return eventually(test, duration, defaultAttempts());
    }

    default Assertion eventually(CheckedSupplier<Assertion> test, int attempts) {
        return eventually(test, defaultDuration(), attempts);
    }

    default Assertion eventually(CheckedSupplier<Assertion> test, Duration duration, int attempts) {
        return new EventualAssertion(test, duration.abs().toMillis(), attempts);
    }

}
