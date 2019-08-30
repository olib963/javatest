package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.JavaTest;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.*;

public class SimpleMatcherTests {
    public static TestSuiteClass passing() {
        return new PassingTests();
    }
    public static TestSuiteClass failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuiteClass {

        @Override
        public Stream<Testable> testables() {
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
    static class FailingTests implements TestSuiteClass {
        @Override
        public Stream<Testable> testables() {
            return Stream.of(
                    test("One add One is Three! (FAIL)", () ->  that(1 + 1, isEqualTo(3))),
                    test("Object is the incorrect type (FAIL)", () -> that("Hello", hasType(int.class))),
                    test("Object is the same instance (FAIL)", () -> that(new Object(), isTheSameInstanceAs(new Object())))
            );
        }
    }
}
