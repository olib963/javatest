package org.javatest.parameterised.functions;

@FunctionalInterface
public interface Function5<A, B, C, D, E, F> {
    F apply(A a, B b, C c, D d, E e);
}
