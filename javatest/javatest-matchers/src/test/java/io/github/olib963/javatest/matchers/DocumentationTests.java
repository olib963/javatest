package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.TestSuiteClass;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.suite;
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

public class DocumentationTests implements TestSuiteClass {

    // tag::simpleTests[]
    public class MatcherTests {
        private static final String TEST_STRING = "Hello World";

        public Collection<Test> matcherTests() {
            return List.of(
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
        public Collection<Test> exceptionTests() {
            return List.of(
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

    // tag::extraMessage[]
    public class ExtraMessaging {

        public Test messagingTest = test("Validation Test", () -> {
                var validator = new MyValidator();
                var toValidate = 1;
                return that(
                        "Because we are validating a number below 10",
                        () -> validator.validate(toValidate),
                        willThrowExceptionThat(hasType(IllegalArgumentException.class)));
            });

        /* Description of test is:
         *
         * Because we are validating a number below 10: Expected {runnable} to
         * throw an exception that was expected to be an instance of {java.lang.IllegalArgumentException}
         */

    }
    // end::extraMessage[]

    @Override
    public Collection<Testable> testables() {
        return List.of(
                suite("Matcher Documentation tests", new MatcherTests().matcherTests()),
                suite("Exception Documentation Tests", new MyExceptionTests().exceptionTests()),
                suite("Custom Matcher Documentation Tests", List.of(new TestTheUniverse().universeTest())),
                new ExtraMessaging().messagingTest
        );
    }

}
