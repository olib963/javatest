package org.javatest.parameterised;

import org.javatest.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Parameterised {

    // TODO I am not sure this is much better. It may be a good idea to wait for java to just support tuples and functions properly, we would likely get better compile errors
    default <A> Stream<Test> parameterised(Stream<A> inputs, Function<A, Test> testFunction) {
        return inputs.map(testFunction);
    }

    default <A, B> Stream<Test> parameterised(Stream<Helpers.Tuple2<A, B>> inputs, BiFunction<A, B, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2));
    }

    default <A, B, C> Stream<Test> parameterised(Stream<Helpers.Tuple3<A, B, C>> inputs, Helpers.Function3<A, B, C, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3));
    }

    default <A, B, C, D> Stream<Test> parameterised(Stream<Helpers.Tuple4<A, B, C, D>> inputs, Helpers.Function4<A, B, C, D, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4));
    }

    default <A, B, C, D, E> Stream<Test> parameterised(Stream<Helpers.Tuple5<A, B, C, D, E>> inputs, Helpers.Function5<A, B, C, D, E, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5));
    }

    default <A, B, C, D, E, F> Stream<Test> parameterised(Stream<Helpers.Tuple6<A, B, C, D, E, F>> inputs, Helpers.Function6<A, B, C, D, E, F, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6));
    }

    default <A, B, C, D, E, F, G> Stream<Test> parameterised(Stream<Helpers.Tuple7<A, B, C, D, E, F, G>> inputs, Helpers.Function7<A, B, C, D, E, F, G, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7));
    }

    default <A, B, C, D, E, F, G, H> Stream<Test> parameterised(Stream<Helpers.Tuple8<A, B, C, D, E, F, G, H>> inputs, Helpers.Function8<A, B, C, D, E, F, G, H, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8));
    }

    default <A, B, C, D, E, F, G, H, I> Stream<Test> parameterised(Stream<Helpers.Tuple9<A, B, C, D, E, F, G, H, I>> inputs, Helpers.Function9<A, B, C, D, E, F, G, H, I, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9));
    }

    default <A, B, C, D, E, F, G, H, I, J> Stream<Test> parameterised(Stream<Helpers.Tuple10<A, B, C, D, E, F, G, H, I, J>> inputs, Helpers.Function10<A, B, C, D, E, F, G, H, I, J, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10));
    }

}
