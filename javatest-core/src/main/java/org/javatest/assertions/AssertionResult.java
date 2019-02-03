package org.javatest.assertions;

import org.javatest.JavaTest;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    public static AssertionResult failed(Throwable error) {
        var stringWriter = new StringWriter();
        stringWriter.append("An exception was thrown during your test.");
        stringWriter.append(JavaTest.SEPARATOR);
        stringWriter.append("Message: ");
        stringWriter.append(error.getMessage());
        stringWriter.append(JavaTest.SEPARATOR);
        error.printStackTrace(new PrintWriter(stringWriter));
        return new AssertionResult(false, stringWriter.toString());
    }

    public static AssertionResult failed(AssertionError error) {
        return new AssertionResult(false,"An assertion error was thrown. This would imply an assertion was made and not returned, please return an assertion instead.");
    }
}
