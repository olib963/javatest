package org.javatest.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

// TODO review logging structure
public class TestLog {
    static final String SEP = System.lineSeparator();
    private final Collection<TestLogEntry> log;
    public TestLog(Collection<TestLogEntry> log) {
        this.log = log;
    }

    public static TestLog init() {
        return new TestLog(new ArrayList<>());
    }

    public TestLog add(TestLogEntry logEntry) {
        log.add(logEntry);
        return this;
    }

    public TestLog addAll(TestLog log) {
        this.log.addAll(log.log);
        return this;
    }

    public String createLogString() {
        return createLogString(0);
    }

    String createLogString(int indent) {
        return log.stream().map(e -> e.createLog(indent)).collect(Collectors.joining(SEP));
    }


    @Override
    public String toString() {
        return "TestLog{" +
                "log=" + log +
                '}';
    }
}
