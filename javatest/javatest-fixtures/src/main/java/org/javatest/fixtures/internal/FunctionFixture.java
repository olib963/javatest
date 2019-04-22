package org.javatest.fixtures.internal;

import org.javatest.CheckedConsumer;
import org.javatest.CheckedSupplier;
import org.javatest.fixtures.Fixture;

public class FunctionFixture<FixtureType> implements Fixture<FixtureType> {
    private final CheckedSupplier<FixtureType> creator;
    private final CheckedConsumer<FixtureType> destroyer;

    public FunctionFixture(CheckedSupplier<FixtureType> creator, CheckedConsumer<FixtureType> destroyer) {
        this.creator = creator;
        this.destroyer = destroyer;
    }

    @Override
    public FixtureType create() throws Throwable {
        return creator.get();
    }

    @Override
    public void destroy(FixtureType fixture) throws Throwable {
        destroyer.accept(fixture);
    }
}
