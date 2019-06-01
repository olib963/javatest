package io.github.olib963.javatest;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
