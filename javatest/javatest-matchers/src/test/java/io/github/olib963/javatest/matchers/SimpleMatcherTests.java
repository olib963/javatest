package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.*;

public class SimpleMatcherTests {
    public static TestSuite passing() {
        return new PassingTests();
    }
    public static TestSuite failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuite {

        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    JavaTest.test("One add One is Two!", () -> that(1 + 1, isEqualTo(2))),
                    test("Object is the correct type", () -> that("Hello", hasType(String.class))),
                    test("Object is the same instance", () -> {
                        var object = new Object();
                        return that(object, isTheSameInstanceAs(object));
                    })
            );
        }
    }
    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                    test("Object is the incorrect type (FAIL)", () -> that("Hello", hasType(int.class))),
                    test("Object is the same instance (FAIL)", () -> that(new Object(), isTheSameInstanceAs(new Object())))
            );
        }
    }
}
