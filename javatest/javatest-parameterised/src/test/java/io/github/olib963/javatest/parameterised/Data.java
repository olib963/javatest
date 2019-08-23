package io.github.olib963.javatest.parameterised;

import io.github.olib963.javatest.parameterised.tuples.Tuple10;
import io.github.olib963.javatest.parameterised.tuples.Tuple3;

import java.util.stream.Stream;

import static io.github.olib963.javatest.parameterised.Parameterised.t;

public class Data {
    private Data() {
    }

    public static Stream<Tuple3<Integer, Integer, Integer>> multiplication() {
        return Stream.of(
                t(1, 1, 1),
                t(2, 4, 8),
                t(3, 9, 27),
                t(4, 16, 64),
                t(5, 25, 125),
                t(10, 100, 1000)
        );
    }

    public static Stream<Tuple10<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> madAddition() {
        return Stream.of(
                t(1, 2, 3, 4, 5, 6, 7, 8, 9, 45),
                t(1, 2, 3, 5, 7, 11, 13, 17, 19, 78),
                t(1, 4, 9, 16, 25, 36, 49, 64, 81, 285)
        );
    }

}
