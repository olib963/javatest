package org.javatest.logging;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO review logging structure
public class TestLogEntry {
    private final String entry;
    private final Colour logColour;
    private final Optional<TestLog> next;

    public TestLogEntry(String entry, Colour logColour) {
        this(entry, logColour, Optional.empty());
    }

    public TestLogEntry(String entry, Colour logColour, Optional<TestLog> next) {
        this.entry = entry;
        this.logColour = logColour;
        this.next = next;
    }

    public String createLog(int indentLevel) {
        var thisLog = logColour.getCode() + IntStream.range(0, indentLevel).mapToObj(i -> "\t").collect(Collectors.joining()) + entry;
        var nextLog = next.map(t -> thisLog + t.createLogString(indentLevel + 1)).orElse(thisLog);
        return nextLog + Colour.resetCode();
    }

    @Override
    public String toString() {
        return "TestLogEntry{" +
                "entry='" + entry + '\'' +
                ", logColour=" + logColour +
                ", next=" + next +
                '}';
    }
}
