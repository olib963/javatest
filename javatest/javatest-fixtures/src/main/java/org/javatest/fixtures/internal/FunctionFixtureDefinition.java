package org.javatest.fixtures.internal;

import org.javatest.fixtures.CheckedConsumer;
import org.javatest.CheckedSupplier;
import org.javatest.fixtures.FixtureDefinition;

public class FunctionFixtureDefinition<FixtureType> implements FixtureDefinition<FixtureType> {
    private final CheckedSupplier<FixtureType> creator;
    private final CheckedConsumer<FixtureType> destroyer;

    public FunctionFixtureDefinition(CheckedSupplier<FixtureType> creator, CheckedConsumer<FixtureType> destroyer) {
        this.creator = creator;
        this.destroyer = destroyer;
    }

    @Override
    public FixtureType create() throws Exception {
        return creator.get();
    }

    @Override
    public void destroy(FixtureType fixture) throws Exception {
        destroyer.accept(fixture);
    }
}
