package io.github.olib963.javatest.parameterised.functions;

@FunctionalInterface
public interface Function3<A, B, C, D> {
    D apply(A a, B b, C c);
}
