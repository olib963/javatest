package org.javatest;

@FunctionalInterface
public interface CheckedSupplier<T> {
    // TODO should probably be Exception not Throwable
    T get() throws Throwable;
}

// TODO Can CheckedSupplier be aliased or renamed, so it makes more sense to someone writing tests, e.g. TestFunction?
