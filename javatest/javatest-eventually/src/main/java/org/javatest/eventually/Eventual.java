package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.CheckedSupplier;
import org.javatest.eventually.internal.EventualAssertion;

import java.time.Duration;

final public class Eventual {

    private Eventual() {}

    public static Assertion eventually(CheckedSupplier<Assertion> test, Duration duration, int attempts) {
        return new EventualAssertion(test, duration.abs().toMillis(), attempts);
    }
}
