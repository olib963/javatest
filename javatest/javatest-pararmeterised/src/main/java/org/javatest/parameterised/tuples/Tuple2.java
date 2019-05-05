package org.javatest.parameterised.tuples;

public class Tuple2<A, B> {
    public final A _1;
    public final B _2;
    public Tuple2(A _1, B _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public <C> Tuple3<A, B, C> add(C _3) {
        return new Tuple3<>(_1, _2, _3);
    }

    public <C, D> Tuple4<A, B, C, D> add(Tuple2<C, D> t) {
        return new Tuple4<>(_1, _2, t._1, t._2);
    }

    public <C, D, E> Tuple5<A, B, C, D, E> add(Tuple3<C, D, E> t) {
        return new Tuple5<>(_1, _2, t._1, t._2, t._3);
    }

    public <C, D, E, F> Tuple6<A, B, C, D, E, F> add(Tuple4<C, D, E, F> t) {
        return new Tuple6<>(_1, _2, t._1, t._2, t._3, t._4);
    }

    public <C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> add(Tuple5<C, D, E, F, G> t) {
        return new Tuple7<>(_1, _2, t._1, t._2, t._3, t._4, t._5);
    }

    public <C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(Tuple6<C, D, E, F, G, H> t) {
        return new Tuple8<>(_1, _2, t._1, t._2, t._3, t._4, t._5, t._6);
    }

    public <C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(Tuple7<C, D, E, F, G, H, I> t) {
        return new Tuple9<>(_1, _2, t._1, t._2, t._3, t._4, t._5, t._6, t._7);
    }

    public <C, D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple8<C, D, E, F, G, H, I, J> t) {
        return new Tuple10<>(_1, _2, t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8);
    }

    @Override
    public String toString() {
        return '(' + _1.toString() + ',' + _2 + ')';
    }
}
