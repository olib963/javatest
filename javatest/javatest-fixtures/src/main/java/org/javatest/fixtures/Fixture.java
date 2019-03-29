package org.javatest.fixtures;

public interface Fixture<F> {

    // TODO is there a way to return a failure without recreating Try? Maybe we should just do that.
    F create() throws Throwable;

    void destroy(F fixture) throws Throwable;
}
