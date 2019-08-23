package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;

// tag::import[]
import static io.github.olib963.javatest.matchers.Matcher.*;
// end::import[]
// tag::stringImport[]
import static io.github.olib963.javatest.matchers.StringMatchers.*;
// end::stringImport[]
// tag::exceptionImport[]
import static io.github.olib963.javatest.matchers.ExceptionMatchers.*;
// end::exceptionImport[]

public class DocumentationTests implements TestSuite {

    // tag::simpleTests[]
    public class MatcherTests {
        private static final String TEST_STRING = "Hello World";

        public Stream<Test> matcherTests() {
            return Stream.of(
                    test("Addition", () -> that(1 + 1, isEqualTo(2))),
                    test("Types", () -> that("Hello", hasType(String.class))),
                    test("String Prefix", () -> that(TEST_STRING, startsWith("Hello"))),
                    test("Substring", () -> that(TEST_STRING, containsString("Wor")))
            );
        }
    }
    // end::simpleTests[]

    // tag::exceptionTests[]
    public class MyExceptionTests {
        public Stream<Test> exceptionTests() {
            return Stream.of(
                    test("Matching an exception", () -> {
                        // Exceptions can be matched on just like any other object
                        var message = "NOT ALLOWED";
                        var exception = new IllegalArgumentException(message);
                        return that(exception, hasMessage(message));
                    }),
                    test("Simple Exception", () -> {
                        // If you want to check an exception is thrown then provide a runnable containing the throwing method
                        var validator = new MyValidator();
                        return that(() -> validator.validate(1),
                                willThrowExceptionThat(hasType(IllegalArgumentException.class)));
                    }),
                    test("Check Message on cause", () -> {
                        var myObject = new MyThrowingObject();
                        // You can compose matchers together
                        var hasCauseWithMessageContainingFoo = hasCauseThat(hasMessageThat(containsString("Foo")));
                        return that(() -> myObject.throwingFunction(10), willThrowExceptionThat(hasCauseWithMessageContainingFoo));
                    }));
        }
    }
    // end::exceptionTests[]

    private class MyValidator {
        public void validate(int value) {
            throw new IllegalArgumentException();
        }
    }

    private class MyThrowingObject {
        public void throwingFunction(int value) {
            throw new IllegalArgumentException("", new IllegalStateException("Foo bar baz"));
        }
    }

    // tag::customMatcher[]
    public class TestTheUniverse {

        private final Matcher<Integer> isFortyTwo =
                Matcher.fromFunctions(i -> i == 42, "be 42");

        public Test universeTest() {
            return test("Meaning", () -> that(new Universe().meaning(), isFortyTwo));
        }

        private class Universe {
            public int meaning() {
                return 42;
            }
        }

    }
    // end::customMatcher[]

    @Override
    public Stream<Test> tests() {
        return Stream.of(
            new MatcherTests().matcherTests(), new MyExceptionTests().exceptionTests(), Stream.of(new TestTheUniverse().universeTest())
        ).flatMap(Function.identity());
    }

}
