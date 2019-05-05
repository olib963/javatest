package org.javatest.parameterised.tuples;

public class Tuple5<A, B, C, D, E> {
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

    public <F> Tuple6<A, B, C, D, E, F> add(F _6) {
        return new Tuple6<>(_1, _2, _3, _4, _5, _6);
    }

    public <F, G> Tuple7<A, B, C, D, E, F, G> add(Tuple2<F, G> t) {
        return new Tuple7<>(_1, _2, _3, _4, _5, t._1, t._2);
    }

    public <F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(Tuple3<F, G, H> t) {
        return new Tuple8<>(_1, _2, _3, _4, _5, t._1, t._2, t._3);
    }

    public <F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(Tuple4<F, G, H, I> t) {
        return new Tuple9<>(_1, _2, _3, _4, _5, t._1, t._2, t._3, t._4);
    }

    public <F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple5<F, G, H, I, J> t) {
        return new Tuple10<>(_1, _2, _3, _4, _5, t._1, t._2, t._3, t._4, t._5);
    }

    @Override
    public String toString() {
        return '(' +_1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ',' + _5 + ')';
    }
}
