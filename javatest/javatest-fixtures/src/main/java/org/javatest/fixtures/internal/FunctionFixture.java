package org.javatest.fixtures.internal;

import org.javatest.CheckedConsumer;
import org.javatest.CheckedSupplier;
import org.javatest.fixtures.Fixture;

public class FunctionFixture<F> implements Fixture<F> {
    private final CheckedSupplier<F> creator;
    private final CheckedConsumer<F> destroyer;

    public FunctionFixture(CheckedSupplier<F> creator, CheckedConsumer<F> destroyer) {
        this.creator = creator;
        this.destroyer = destroyer;
    }

    @Override
    public F create() throws Throwable {
        return creator.get();
    }

    @Override
    public void destroy(F fixture) throws Throwable {
        destroyer.accept(fixture);
    }
}
