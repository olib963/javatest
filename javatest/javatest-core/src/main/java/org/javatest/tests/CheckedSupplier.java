package org.javatest.tests;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Throwable;
}
