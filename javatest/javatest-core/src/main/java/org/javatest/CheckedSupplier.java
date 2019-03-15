package org.javatest;

@FunctionalInterface
public interface CheckedSupplier<T> {
    // TODO should probably be Exception not Throwable
    T get() throws Throwable;
}
