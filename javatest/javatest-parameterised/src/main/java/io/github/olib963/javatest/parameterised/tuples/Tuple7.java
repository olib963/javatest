package io.github.olib963.javatest.parameterised.tuples;

final public class Tuple7<A, B, C, D, E, F, G> {
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

    public <H> Tuple8<A, B, C, D, E, F, G, H> add(H _8) {
        return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public <H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(Tuple2<H, I> t) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, _7, t._1, t._2);
    }

    public <H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(Tuple3<H, I, J> t) {
        return new Tuple10<>(_1, _2, _3, _4, _5, _6, _7, t._1, t._2, t._3);
    }

    @Override
    public String toString() {
        return '(' + _1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ',' + _5 + ',' + _6 + ',' + _7 + ')';
    }
}
