package org.javatest.assertions;

import java.util.Optional;

public class AssertionResult {

    public final boolean holds;
    public final boolean pending;
    public final Optional<String> description;

    public AssertionResult(boolean holds, String description) {
        this(holds, Optional.of(description), false);
    }

    // Internal only
    AssertionResult(boolean holds, Optional<String> description, boolean pending) {
        this.holds = holds;
        this.description = description;
        this.pending = pending;
    }

    // TODO add description
    public static AssertionResult failed(Exception error) {
        return new AssertionResult(false, Optional.empty(), false);
    }

    // TODO add description
    // TODO treat this differently, this should be invalid.
    public static AssertionResult failed(AssertionError error) {
        return new AssertionResult(false, Optional.empty(), false);
    }
}
