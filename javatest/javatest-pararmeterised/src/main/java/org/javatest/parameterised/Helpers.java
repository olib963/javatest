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

    static <A, B> Helpers.Tuple2<A, B> t(A _1, B _2) {
        return new Helpers.Tuple2<>(_1, _2);
    }

    public static class Tuple2<A, B> {
        public final A _1;
        public final B _2;
        public Tuple2(A _1, B _2) {
            this._1 = _1;
            this._2 = _2;
        }
    }

    static <A, B, C> Helpers.Tuple3<A, B, C> t(A _1, B _2, C _3) {
        return new Helpers.Tuple3<>(_1, _2, _3);
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

    static <A, B, C, D> Helpers.Tuple4<A, B, C, D> t(A _1, B _2, C _3, D _4) {
        return new Helpers.Tuple4<>(_1, _2, _3, _4);
    }

    public static class Tuple4<A, B, C, D> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public Tuple4(A _1, B _2, C _3, D _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }
    }

    static <A, B, C, D, E> Helpers.Tuple5<A, B, C, D, E> t(A _1, B _2, C _3, D _4, E _5) {
        return new Helpers.Tuple5<>(_1, _2, _3, _4, _5);
    }

    public static class Tuple5<A, B, C, D, E> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public final E _5;
        public Tuple5(A _1, B _2, C _3, D _4, E _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }
    }

    static <A, B, C, D, E, F> Helpers.Tuple6<A, B, C, D, E, F> t(A _1, B _2, C _3, D _4, E _5, F _6) {
        return new Helpers.Tuple6<>(_1, _2, _3, _4, _5, _6);
    }

    public static class Tuple6<A, B, C, D, E, F> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public final E _5;
        public final F _6;
        public Tuple6(A _1, B _2, C _3, D _4, E _5, F _6) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
        }
    }

    static <A, B, C, D, E, F, G> Helpers.Tuple7<A, B, C, D, E, F, G> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7) {
        return new Helpers.Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
    }

    public static class Tuple7<A, B, C, D, E, F, G> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public final E _5;
        public final F _6;
        public final G _7;
        public Tuple7(A _1, B _2, C _3, D _4, E _5, F _6, G _7) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
        }
    }

    static <A, B, C, D, E, F, G, H> Helpers.Tuple8<A, B, C, D, E, F, G, H> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8) {
        return new Helpers.Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static class Tuple8<A, B, C, D, E, F, G, H> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public final E _5;
        public final F _6;
        public final G _7;
        public final H _8;
        public Tuple8(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
            this._8 = _8;
        }
    }

    static <A, B, C, D, E, F, G, H, I> Helpers.Tuple9<A, B, C, D, E, F, G, H, I> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8, I _9) {
        return new Helpers.Tuple9<>(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public static class Tuple9<A, B, C, D, E, F, G, H, I> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public final E _5;
        public final F _6;
        public final G _7;
        public final H _8;
        public final I _9;
        public Tuple9(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8, I _9) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
            this._8 = _8;
            this._9 = _9;
        }
    }

    static <A, B, C, D, E, F, G, H, I, J> Helpers.Tuple10<A, B, C, D, E, F, G, H, I, J> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8, I _9, J _10) {
        return new Helpers.Tuple10<>(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
    }

    public static class Tuple10<A, B, C, D, E, F, G, H, I, J> {
        public final A _1;
        public final B _2;
        public final C _3;
        public final D _4;
        public final E _5;
        public final F _6;
        public final G _7;
        public final H _8;
        public final I _9;
        public final J _10;
        public Tuple10(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8, I _9, J _10) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
            this._8 = _8;
            this._9 = _9;
            this._10 = _10;
        }
    }
}
