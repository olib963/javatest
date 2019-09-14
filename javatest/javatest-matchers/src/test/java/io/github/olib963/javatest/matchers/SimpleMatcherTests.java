package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable.TestSuite;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.*;

public class SimpleMatcherTests {

    public static TestSuite suite() {
        return Utils.matcherSuite("Simple Matcher Tests",
                Stream.of(
                        test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
                        test("Object is the correct type", () -> that("Hello", hasType(String.class))),
                        test("Object is the same instance", () -> {
                            var object = new Object();
                            return that(object, isTheSameInstanceAs(object));
                        }),
                        test("One add One is the integer two!", () -> that(1 + 1, isEqualTo(2).and(hasType(Integer.class))))
                ),
                Stream.of(
                        test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                        test("Object is the incorrect type (FAIL)", () -> that("Hello", hasType(int.class))),
                        test("Object is the same instance (FAIL)", () -> that(new Object(), isTheSameInstanceAs(new Object()))),
                        test("One add One is the long two (FAIL)", () -> that(1 + 1, isEqualTo(2).and(hasType(Long.class)))),
                        test("One add One is the integer three (FAIL)", () -> that(1 + 1, isEqualTo(3).and(hasType(Integer.class))))
                )
                );
    }
}
