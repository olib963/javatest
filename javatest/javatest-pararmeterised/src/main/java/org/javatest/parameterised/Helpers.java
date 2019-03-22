package org.javatest.parameterised;

public class Helpers {
    private Helpers() {}

    // TODO more can be added if people need them for testing, scala goes to 22 but I thought 10 was fine for now.
    @FunctionalInterface
    public interface Function3<A, B, C, D> {
        D apply(A a, B b, C c);
    }

    @FunctionalInterface
    public interface Function4<A, B, C, D, E> {
        E apply(A a, B b, C c, D d);
    }

    @FunctionalInterface
    public interface Function5<A, B, C, D, E, F> {
        F apply(A a, B b, C c, D d, E e);
    }

    @FunctionalInterface
    public interface Function6<A, B, C, D, E, F, G> {
        G apply(A a, B b, C c, D d, E e, F f);
    }

    @FunctionalInterface
    public interface Function7<A, B, C, D, E, F, G, H> {
        H apply(A a, B b, C c, D d, E e, F f, G g);
    }

    @FunctionalInterface
    public interface Function8<A, B, C, D, E, F, G, H, I> {
        I apply(A a, B b, C c, D d, E e, F f, G g, H h);
    }

    @FunctionalInterface
    public interface Function9<A, B, C, D, E, F, G, H, I, J> {
        J apply(A a, B b, C c, D d, E e, F f, G g, H h, I i);
    }

    @FunctionalInterface
    public interface Function10<A, B, C, D, E, F, G, H, I, J, K> {
        K apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);
    }

    static <A, B> Helpers.Tuple2<A, B> t(A a, B b) {
        return new Helpers.Tuple2<>(a, b);
    }

    public static class Tuple2<A, B> {
        public final A _1;
        public final B _2;
        public Tuple2(A _1, B _2) {
            this._1 = _1;
            this._2 = _2;
        }
    }

    static <A, B, C> Helpers.Tuple3<A, B, C> t(A a, B b, C c) {
        return new Helpers.Tuple3<>(a, b, c);
    }

    public static class Tuple3<A, B, C> {
        public final A _1;
        public final B _2;
        public final C _3;
        public Tuple3(A _1, B _2, C _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }
    }
}
