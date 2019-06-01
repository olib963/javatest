package io.github.olib963.javatest.parameterised.tuples;

final public class Tuple6<A, B, C, D, E, F> {
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

    public <G> Tuple7<A, B, C, D, E, F, G> add(G _7) {
        return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
    }

    public <G, H> Tuple8<A, B, C, D, E, F, G, H> add(Tuple2<G, H> t) {
        return new Tuple8<>(_1, _2, _3, _4, _5, _6, t._1, t._2);
    }

    public <G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(Tuple3<G, H, I> t) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, t._1, t._2, t._3);
    }

    public <G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple4<G, H, I, J> t) {
        return new Tuple10<>(_1, _2, _3, _4, _5, _6, t._1, t._2, t._3, t._4);
    }

    @Override
    public String toString() {
        return '(' +_1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ',' + _5 + ',' + _6 + ')';
    }
}
