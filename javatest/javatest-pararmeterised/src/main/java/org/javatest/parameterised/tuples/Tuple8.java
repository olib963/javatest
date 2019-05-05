package org.javatest.parameterised.tuples;

final public class Tuple8<A, B, C, D, E, F, G, H> {
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

    public <I> Tuple9<A, B, C, D, E, F, G, H, I> add(I _9) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public <I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple2<I, J> t) {
        return new Tuple10<>(_1, _2, _3, _4, _5, _6, _7, _8, t._1, t._2);
    }

    @Override
    public String toString() {
        return '(' + _1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ',' + _5 + ',' + _6 + ',' + _7 + ',' + _8 + ')';
    }
}
