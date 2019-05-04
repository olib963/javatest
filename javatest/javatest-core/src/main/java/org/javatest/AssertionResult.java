package org.javatest;

import org.javatest.logging.LoggingObserver;

import java.io.PrintWriter;
import java.io.StringWriter;

final public class AssertionResult {

    public final boolean holds;
    public final boolean pending;
    public final String description;

    private AssertionResult(boolean holds, String description, boolean pending) {
        this.holds = holds;
        this.description = description;
        this.pending = pending;
    }

    public static AssertionResult exception(Throwable error) {
        var stringWriter = new StringWriter();
        stringWriter.append("An exception was thrown during your test.");
        stringWriter.append(LoggingObserver.SEPARATOR);
        stringWriter.append("Message: ");
        stringWriter.append(error.getMessage());
        stringWriter.append(LoggingObserver.SEPARATOR);
        // TODO stack trace trim to the entry point of the test if possible
        error.printStackTrace(new PrintWriter(stringWriter));
        return new AssertionResult(false, stringWriter.toString(), false);
    }

    public static AssertionResult assertionThrown(AssertionError error) {
        return new AssertionResult(false, "An assertion error was thrown. This would imply an assertion was made and not returned, please return an assertion instead.", false);
    }

    public static AssertionResult pending(String description) {
        return new AssertionResult(true, description, true);
    }

    public static AssertionResult of(boolean holds, String description) {
        return new AssertionResult(holds, description, false);
    }
}
