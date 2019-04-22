package org.javatest.fixtures;

// TODO come up with intuitive name for this interface
public interface Fixture<FixtureType> {

    // TODO is there a way to return a failure without recreating Try? Maybe we should just do that.
    FixtureType create() throws Throwable;

    void destroy(FixtureType fixture) throws Throwable;
}
