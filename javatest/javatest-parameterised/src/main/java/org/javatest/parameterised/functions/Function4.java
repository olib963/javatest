package org.javatest.parameterised.functions;

@FunctionalInterface
public interface Function4<A, B, C, D, E> {
    E apply(A a, B b, C c, D d);
}
