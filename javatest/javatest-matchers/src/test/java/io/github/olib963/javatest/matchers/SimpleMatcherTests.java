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
                        test("Null is Null!", () -> that(null, isEqualTo(null))),
                        test("Object is the correct type", () -> that("Hello", hasType(String.class))),
                        test("Object is the same instance", () -> {
                            var object = new Object();
                            return that(object, isTheSameInstanceAs(object));
                        }),
                        test("One add One is the integer two!", () -> that(1 + 1, isEqualTo(2).and(hasType(Integer.class)))),
                        test("One add One is not Three!", () -> that(1 + 1, not(isEqualTo(3)))),
                        test("Object is not the correct type", () -> that("Hello", not(hasType(Boolean.class)))),
                        test("Object is not the same instance", () -> that(new Object(), not(isTheSameInstanceAs(new Object()))))
                ),
                Stream.of(
                        test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                        test("Null is not null! (FAIL)", () ->  that(null, isEqualTo(1))),
                        test("Not null is null! (FAIL)", () ->  that(1, isEqualTo(null))),
                        test("Object is the incorrect type (FAIL)", () -> that("Hello", hasType(int.class))),
                        test("Object is the same instance (FAIL)", () -> that(new Object(), isTheSameInstanceAs(new Object()))),
                        test("One add One is the long two (FAIL)", () -> that(1 + 1, isEqualTo(2).and(hasType(Long.class)))),
                        test("One add One is the integer three (FAIL)", () -> that(1 + 1, isEqualTo(3).and(hasType(Integer.class)))),
                        test("One add One is not Two! (FAIL)", () -> that(1 + 1, not(isEqualTo(2)))),
                        test("Object is not the correct type (FAIL)", () -> that("Hello", not(hasType(String.class)))),
                        test("Object is not the same instance (FAIL)", () -> {
                            var object = new Object();
                            return that(object, not(isTheSameInstanceAs(object)));
                        })
                )
                );
    }
}
