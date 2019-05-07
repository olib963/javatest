package org.javatest.eventually;

import org.javatest.Assertion;
import org.javatest.CheckedSupplier;
import org.javatest.eventually.internal.EventualAssertion;

public class Eventually {

    private Eventually() {}

    public static Assertion eventually(CheckedSupplier<Assertion> test) {
        return eventually(test, EventualConfig.DEFAULT_CONFIG);
    }

    public static Assertion eventually(CheckedSupplier<Assertion> test, EventualConfig config) {
        return new EventualAssertion(test,
                config.waitInterval.abs().toMillis(),
                config.attempts,
                config.initialDelay.map(d -> d.abs().toMillis()));
    }
}
