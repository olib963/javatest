package io.github.olib963.javatest.parameterised.tuples;

final public class Tuple10<A, B, C, D, E, F, G, H, I, J> {
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

    @Override
    public String toString() {
        return '(' + _1.toString() + ',' + _2 + ',' + _3 + ',' + _4 + ',' + _5 + ',' + _6 + ',' + _7 + ',' + _8 + ',' + _9 + ',' + _10 + ')';
    }
}
