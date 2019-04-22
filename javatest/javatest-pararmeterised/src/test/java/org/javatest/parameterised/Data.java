package org.javatest.parameterised;

import java.util.stream.Stream;

import static org.javatest.parameterised.Helpers.t;

public class Data implements Parameterised {
    private Data() {
    }

    public static Stream<String> palindromes() {
        return Stream.of(
                "Anna",
                "Aibohphobia",
                "Civic",
                "Deified",
                "Kayak",
                "Level",
                "Madam",
                "Mom",
                "Noon",
                "Racecar",
                "Radar",
                "Redder",
                "Refer",
                "Repaper",
                "Rotator",
                "Rotor",
                "Sagas",
                "Solos",
                "Stats",
                "Tacocat",
                "Tenet",
                "Wow"
        );
    }

    public static Stream<Helpers.Tuple2<Integer, Long>> fibonacci() {
        return Stream.of(
                t(0, 0L),
                t(1, 1L),
                t(2, 1L),
                t(3, 2L),
                t(4, 3L),
                t(5, 5L),
                t(6, 8L),
                t(8, 21L),
                t(10, 55L),
                t(60, 1548008755920L),
                t(90, 2880067194370816120L)
        );
    }


    public static Stream<Helpers.Tuple3<Integer, Integer, Integer>> multiplication() {
        return Stream.of(
                t(1, 1, 1),
                t(2, 4, 8),
                t(3, 9, 27),
                t(4, 16, 64),
                t(5, 25, 125),
                t(10, 100, 1000)
        );
    }

    public static Stream<Helpers.Tuple10<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> madAddition() {
        return Stream.of(
                t(1, 2, 3, 4, 5, 6, 7, 8, 9, 45),
                t(1, 2, 3, 5, 7, 11, 13, 17, 19, 78),
                t(1, 4, 9, 16, 25, 36, 49, 64, 81, 285)
        );
    }

}
