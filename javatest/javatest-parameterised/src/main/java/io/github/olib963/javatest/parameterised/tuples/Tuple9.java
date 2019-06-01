package io.github.olib963.javatest.parameterised.tuples;

final public class Tuple9<A, B, C, D, E, F, G, H, I> {
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

    public <J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(J _10) {
        return new Tuple10<>(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
    }

    @Override
    public String toString() {
        return '(' + _1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ',' + _5 + ',' + _6 + ',' + _7 + ',' + _8 + ',' + _9 + ')';
    }
}
