package org.javatest;

@FunctionalInterface
public interface CheckedConsumer<T> {
    // TODO should be Exception
    void accept(T t) throws Throwable;
}
