package org.javatest.parameterised.functions;

@FunctionalInterface
public interface Function8<A, B, C, D, E, F, G, H, I> {
    I apply(A a, B b, C c, D d, E e, F f, G g, H h);
}
