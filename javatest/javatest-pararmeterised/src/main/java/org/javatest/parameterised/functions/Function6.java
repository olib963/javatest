package org.javatest.parameterised.functions;

@FunctionalInterface
public interface Function6<A, B, C, D, E, F, G> {
    G apply(A a, B b, C c, D d, E e, F f);
}
