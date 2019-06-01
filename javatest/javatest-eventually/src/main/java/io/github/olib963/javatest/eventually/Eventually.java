package io.github.olib963.javatest.eventually;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.CheckedSupplier;
import io.github.olib963.javatest.eventually.internal.EventualAssertion;

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
