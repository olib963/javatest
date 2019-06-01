package io.github.olib963.javatest.parameterised.tuples;

final public class Tuple4<A, B, C, D> {
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

    public <E> Tuple5<A, B, C, D, E> add(E _5) {
        return new Tuple5<>(_1, _2, _3, _4, _5);
    }

    public <E, F> Tuple6<A, B, C, D, E, F> add(Tuple2<E, F> t) {
        return new Tuple6<>(_1, _2, _3, _4, t._1, t._2);
    }

    public <E, F, G> Tuple7<A, B, C, D, E, F, G> add(Tuple3<E, F, G> t) {
        return new Tuple7<>(_1, _2, _3, _4, t._1, t._2, t._3);
    }

    public <E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(Tuple4<E, F, G, H> t) {
        return new Tuple8<>(_1, _2, _3, _4, t._1, t._2, t._3, t._4);
    }

    public <E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(Tuple5<E, F, G, H, I> t) {
        return new Tuple9<>(_1, _2, _3, _4, t._1, t._2, t._3, t._4, t._5);
    }

    public <E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple6<E, F, G, H, I, J> t) {
        return new Tuple10<>(_1, _2, _3, _4, t._1, t._2, t._3, t._4, t._5, t._6);
    }

    @Override
    public String toString() {
        return '(' +_1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ')';
    }
}
