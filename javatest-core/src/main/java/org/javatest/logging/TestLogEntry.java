package org.javatest.logging;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO review logging structure
public class TestLogEntry {
    private final String entry;
    private final Colour logColour;
    private final Collection<TestLogEntry> otherLogs;

    public TestLogEntry(String entry, Colour logColour) {
        this(entry, logColour, Collections.emptyList());
    }

    public TestLogEntry(String entry, Colour logColour, Collection<TestLogEntry> otherLogs) {
        this.entry = entry;
        this.logColour = logColour;
        this.otherLogs = otherLogs;
    }

    public String createLog(int indentLevel) {
        var thisLog = logColour.getCode() + IntStream.range(0, indentLevel).mapToObj(i -> "\t").collect(Collectors.joining()) + entry;
        if(otherLogs.isEmpty()) {
            return thisLog + Colour.resetCode();
        }
        var nestedLogs = otherLogs.stream().map(t -> t.createLog(indentLevel + 1)).collect(Collectors.joining(TestLog.SEP));
        return thisLog + TestLog.SEP + nestedLogs + Colour.resetCode();
    }

    @Override
    public String toString() {
        return "TestLogEntry{" +
                "entry='" + entry + '\'' +
                ", logColour=" + logColour +
                ", otherLogs=" + otherLogs +
                '}';
    }
}
