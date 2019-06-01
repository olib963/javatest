package io.github.olib963.javatest.matchers;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}
