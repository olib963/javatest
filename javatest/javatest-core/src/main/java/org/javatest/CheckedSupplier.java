package org.javatest;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
