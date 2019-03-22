package org.javatest.parameterised;

import org.javatest.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Parameterised {

    default <A> Stream<Test> parameterised(Stream<A> inputs, Function<A, Test> testFunction) {
        return inputs.map(testFunction);
    }

    default <A, B> Stream<Test> parameterised(Stream<Helpers.Tuple2<A, B>> inputs, BiFunction<A, B, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2));
    }

    default <A, B, C> Stream<Test> parameterised(Stream<Helpers.Tuple3<A, B, C>> inputs, Helpers.Function3<A, B, C, Test> testFunction) {
        return inputs.map(t -> testFunction.apply(t._1, t._2, t._3));
    }

}
