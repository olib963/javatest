package org.javatest.fixtures;

public interface FixtureDefinition<Fixture> {

    // TODO is there a way to return a failure without recreating Try? Maybe we should just do that.
    Fixture create() throws Throwable;

    void destroy(Fixture fixture) throws Throwable;
}