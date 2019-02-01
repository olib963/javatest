package org.javatest.matchers;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}
