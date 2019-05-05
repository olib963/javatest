package org.javatest.parameterised.tuples;

final public class Tuple3<A, B, C> {
    public final A _1;
    public final B _2;
    public final C _3;
    public Tuple3(A _1, B _2, C _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public <D> Tuple4<A, B, C, D> add(D _4) {
        return new Tuple4<>(_1, _2, _3, _4);
    }

    public <D, E> Tuple5<A, B, C, D, E> add(Tuple2<D, E> t) {
        return new Tuple5<>(_1, _2, _3, t._1, t._2);
    }

    public <D, E, F> Tuple6<A, B, C, D, E, F> add(Tuple3<D, E, F> t) {
        return new Tuple6<>(_1, _2, _3, t._1, t._2, t._3);
    }

    public <D, E, F, G> Tuple7<A, B, C, D, E, F, G> add(Tuple4<D, E, F, G> t) {
        return new Tuple7<>(_1, _2, _3, t._1, t._2, t._3, t._4);
    }

    public <D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(Tuple5<D, E, F, G, H> t) {
        return new Tuple8<>(_1, _2, _3, t._1, t._2, t._3, t._4, t._5);
    }

    public <D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(Tuple6<D, E, F, G, H, I> t) {
        return new Tuple9<>(_1, _2, _3, t._1, t._2, t._3, t._4, t._5, t._6);
    }

    public <D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple7<D, E, F, G, H, I, J> t) {
        return new Tuple10<>(_1, _2, _3, t._1, t._2, t._3, t._4, t._5, t._6, t._7);
    }

    @Override
    public String toString() {
        return '(' +_1.toString() + ',' + _2 + ',' + _3 + ')';
    }
}
