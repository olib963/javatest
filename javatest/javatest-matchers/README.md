# Matchers

This module contains `Assertion`s that are based on common `Matcher`s, they provide both the check and description for your tests. 
By statically importing `Matcher` you will inherit a few default `Matcher`s and the ability to create `Matcher` based `Assertion`s. Other `Matcher`s are
able to be imported into your tests from various classes e.g. `StringMatchers` which contains `String` specific `Matcher`s.

```java
import static io.github.olib963.javatest.matchers.Matcher.*;
import static io.github.olib963.javatest.matchers.StringMatchers.*;

public class MyTests implements TestSuite {
    private static final String TEST_STRING = "Hello World";
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Addition", () -> that(1 + 1, isEqualTo(2))),
                test("Types", () -> that("Hello", hasType(String.class))),
                test("String Prefix", () -> that(TEST_STRING, startsWith("Hello"))),
                test("Substring", () -> that(TEST_STRING, containsString("Wor")))
        );
    }
}
```

Existing `Matcher` collections:

* `CollectionMatchers`
* `ComparableMatchers`
* `ExceptionMatchers` [see below](#exception-matching)
* `MapMatchers`
* `OptionalMatchers`
* `StringMatchers`

## Exception Matching

While in general I prefer error values to exceptions, I understand there are those that disagree or just have to work with
exception driven Java APIs so I have added `Matcher` syntax for exceptions. These should still be returned as an assertion and will
ensure that the test will only pass if the matched exception is thrown from the expected code block.

```java
import static io.github.olib963.javatest.matchers.Matcher.*;
import static io.github.olib963.javatest.matchers.StringMatchers.*;
import static io.github.olib963.javatest.matchers.ExceptionMatchers.*;

public class MyExceptionTests implements TestSuite {
    @Override
    public Stream<Test> testStream() { 
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

```

## Custom Matchers

You can obviously provide your own `Matcher`s by implementing the interface, but there is a convenience function to allow you 
to create simple `Matcher`s from functions, for example:

```java
import static io.github.olib963.javatest.matchers.Matcher.*;

public class TestTheUniverse implements TestSuite {
    
    private final Matcher<Integer> isFortyTwo =
        Matcher.fromFunctions((Integer i) -> i == 42, "be 42");
    
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Meaning", () -> that(new Universe().meaning(), isFortyTwo))
        );
    }
    
}
```

_______

You can include this module with this dependency:

```xml
<dependency>
    <groupId>io.github.olib963</groupId>
    <artifactId>javatest-matchers</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```

## TODO

- [ ] Create a `toString` utility. Currently arrays etc. are used with matchers the descriptions are less than useful. 
- [ ] Create an abstraction for composite matchers.
- [ ] Provide the ability to add extra descriptions to matcher assertions.
- [ ] Decide how to handle null. At the moment many `that(null, $matcher)` expressions fail tests with NPEs, maybe this is good enough?
Maybe I should explicitly fail if null is passed where I don't want it to be?
- [ ] Try to provide a simple `not()` negatable `Matcher` if possible.
- [ ] Extend with more matchers. Suggestions include: `List`s, `Set`s, `Class`es, `File`s, more `Collection` ones e.g. `containsAnElementThat(Matcher[T])`

