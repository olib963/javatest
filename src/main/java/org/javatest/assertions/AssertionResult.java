package org.javatest.assertions;

import java.util.Collection;
import java.util.Collections;

public class AssertionResult {

    public final boolean holds;
    public final boolean pending;
    public final Collection<String> extraLogs; // TODO likely need to be TestLog or TestLogEntry.

    public AssertionResult(boolean holds) {
        this(holds, Collections.emptyList());
    }

    public AssertionResult(boolean holds, Collection<String> extraLogs) {
        this(holds, extraLogs, false);
    }

    // Internal only
    AssertionResult(boolean holds, Collection<String> extraLogs, boolean pending) {
        this.holds = holds;
        this.extraLogs = extraLogs;
        this.pending = pending;
    }

    public static AssertionResult failed(Exception error) {
        return new AssertionResult(false);
    }

    public static AssertionResult failed(Error error) {
        return new AssertionResult(false);
    }
}
