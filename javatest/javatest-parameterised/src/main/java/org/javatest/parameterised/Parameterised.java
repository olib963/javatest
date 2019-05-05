package org.javatest.parameterised;

import org.javatest.Test;
import org.javatest.parameterised.tuples.*;
import org.javatest.parameterised.functions.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class Parameterised {

    // TODO see if there is a plan to integrate value types, tuples and better functions into Java. If so, these types could be replaced.
    private Parameterised() {
    }

    public static <A> Stream<Test> parameterised(Stream<A> inputs, Function<A, Test> testFunction) {
        return inputs.map(testFunction);
    }

    public static <A, B> Stream<Test> parameterised(Stream<Tuple2<A, B>> inputs, BiFunction<A, B, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2));
    }

    public static <A, B, C> Stream<Test> parameterised(Stream<Tuple3<A, B, C>> inputs, Function3<A, B, C, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3));
    }

    public static <A, B, C, D> Stream<Test> parameterised(Stream<Tuple4<A, B, C, D>> inputs, Function4<A, B, C, D, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4));
    }

    public static <A, B, C, D, E> Stream<Test> parameterised(Stream<Tuple5<A, B, C, D, E>> inputs, Function5<A, B, C, D, E, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5));
    }

    public static <A, B, C, D, E, F> Stream<Test> parameterised(Stream<Tuple6<A, B, C, D, E, F>> inputs, Function6<A, B, C, D, E, F, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6));
    }

    public static <A, B, C, D, E, F, G> Stream<Test> parameterised(Stream<Tuple7<A, B, C, D, E, F, G>> inputs, Function7<A, B, C, D, E, F, G, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7));
    }

    public static <A, B, C, D, E, F, G, H> Stream<Test> parameterised(Stream<Tuple8<A, B, C, D, E, F, G, H>> inputs, Function8<A, B, C, D, E, F, G, H, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8));
    }

    public static <A, B, C, D, E, F, G, H, I> Stream<Test> parameterised(Stream<Tuple9<A, B, C, D, E, F, G, H, I>> inputs, Function9<A, B, C, D, E, F, G, H, I, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9));
    }

    public static <A, B, C, D, E, F, G, H, I, J> Stream<Test> parameterised(Stream<Tuple10<A, B, C, D, E, F, G, H, I, J>> inputs, Function10<A, B, C, D, E, F, G, H, I, J, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10));
    }

    public static <A, B> Tuple2<A, B> t(A _1, B _2) {
        return new Tuple2<>(_1, _2);
    }

    public static <A, B, C> Tuple3<A, B, C> t(A _1, B _2, C _3) {
        return new Tuple3<>(_1, _2, _3);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> t(A _1, B _2, C _3, D _4) {
        return new Tuple4<>(_1, _2, _3, _4);
    }

    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> t(A _1, B _2, C _3, D _4, E _5) {
        return new Tuple5<>(_1, _2, _3, _4, _5);
    }

    public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> t(A _1, B _2, C _3, D _4, E _5, F _6) {
        return new Tuple6<>(_1, _2, _3, _4, _5, _6);
    }

    public static <A, B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7) {
        return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
    }

    public static <A, B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8) {
        return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static <A, B, C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8, I _9) {
        return new Tuple9<>(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public static <A, B, C, D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> t(A _1, B _2, C _3, D _4, E _5, F _6, G _7, H _8, I _9, J _10) {
        return new Tuple10<>(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
    }

}
