package org.javatest.assertions;

public class AssertionResult {

    public final boolean holds;
    public final boolean pending;

    public AssertionResult(boolean holds) {
        this(holds, false);
    }

    // Internal only
    AssertionResult(boolean holds, boolean pending) {
        this.holds = holds;
        this.pending = pending;
    }

    public static AssertionResult failed(Exception error) {
        return new AssertionResult(false);
    }

    public static AssertionResult failed(Error error) {
        return new AssertionResult(false);
    }
}
