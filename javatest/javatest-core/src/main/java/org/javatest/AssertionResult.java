package org.javatest;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AssertionResult {

    public final boolean holds;
    public final boolean pending;
    public final String description;

    private AssertionResult(boolean holds, String description, boolean pending) {
        this.holds = holds;
        this.description = description;
        this.pending = pending;
    }

    static AssertionResult failed(Throwable error) {
        var stringWriter = new StringWriter();
        stringWriter.append("An exception was thrown during your test.");
        stringWriter.append(JavaTest.SEPARATOR);
        stringWriter.append("Message: ");
        stringWriter.append(error.getMessage());
        stringWriter.append(JavaTest.SEPARATOR);
        error.printStackTrace(new PrintWriter(stringWriter));
        return new AssertionResult(false, stringWriter.toString(), false);
    }

    static AssertionResult failed(AssertionError error) {
        return new AssertionResult(false, "An assertion error was thrown. This would imply an assertion was made and not returned, please return an assertion instead.", false);
    }

    public static AssertionResult pending(String description) {
        return new AssertionResult(true, description, true);
    }

    public static AssertionResult of(boolean holds, String description) {
        return new AssertionResult(holds, description, false);
    }
}
