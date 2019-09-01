package io.github.olib963.javatest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import static io.github.olib963.javatest.logging.RunLoggingObserver.SEPARATOR;

public final class AssertionResult {

    public final boolean holds;
    public final boolean pending;
    public final String description;
    private final Collection<String> logs;

    private AssertionResult(boolean holds, String description, boolean pending, Collection<String> logs) {
        this.holds = holds;
        this.description = description;
        this.pending = pending;
        this.logs = logs;
    }

    public Stream<String> logs() {
        return logs.stream();
    }

    public static AssertionResult exception(Exception error) {
        var stringWriter = new StringWriter();
        stringWriter.append("An exception was thrown during your test.");
        stringWriter.append(SEPARATOR);
        stringWriter.append("Message: ");
        stringWriter.append(error.getMessage());
        stringWriter.append(SEPARATOR);
        error.printStackTrace(new PrintWriter(stringWriter));
        return new AssertionResult(false, stringWriter.toString(), false, Collections.emptyList());
    }

    public static AssertionResult assertionThrown(AssertionError error) {
        return new AssertionResult(false, "An assertion error was thrown. This would imply an assertion was made and not returned, please return an assertion instead.", false, Collections.emptyList());
    }

    public static AssertionResult pending(String description) {
        return new AssertionResult(true, description, true, Collections.emptyList());
    }

    public static AssertionResult of(boolean holds, String description) {
        return of(holds, description, Collections.emptyList());
    }
    public static AssertionResult of(boolean holds, String description, Collection<String> logs) {
        return new AssertionResult(holds, description, false, logs);
    }

    public static AssertionResult failure(String description) {
        return of(false, description);
    }

    public static AssertionResult success(String description) {
        return of(true, description);
    }

    @Override
    public String toString() {
        return "AssertionResult{" +
                "holds=" + holds +
                ", pending=" + pending +
                ", description='" + description + '\'' +
                '}';
    }
}
