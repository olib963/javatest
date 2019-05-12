package org.javatest.fixtures.internal;

import org.javatest.fixtures.FixtureDefinition;
import org.javatest.fixtures.Try;

import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionFixtureDefinition<FixtureType> implements FixtureDefinition<FixtureType> {
    private final Supplier<Try<FixtureType>> creator;
    private final Function<FixtureType, Try<Void>> destroyer;

    public FunctionFixtureDefinition(Supplier<Try<FixtureType>> creator, Function<FixtureType, Try<Void>> destroyer) {
        this.creator = creator;
        this.destroyer = destroyer;
    }

    @Override
    public Try<FixtureType> create() {
        return creator.get();
    }

    @Override
    public Try<Void> destroy(FixtureType fixture) {
        return destroyer.apply(fixture);
    }
}
