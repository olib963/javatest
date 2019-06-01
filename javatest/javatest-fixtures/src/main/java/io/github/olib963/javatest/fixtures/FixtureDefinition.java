package io.github.olib963.javatest.fixtures;

public interface FixtureDefinition<Fixture> {

    Try<Fixture> create();

    Try<Void> destroy(Fixture fixture);
}
