# JavaTest

Experimental attempt at a different style of test framework. Very much a work in progress. Any feedback/constructive criticism
is appreciated. I am sure I have not got the API or library right on the first try so there may be breaking changes in the future.

## Basic Principles

- org.javatest.eventually.Tests should be written in plain, simple functional Java with no magic.
- Each test should return one assertion.
- JavaTest will run a stream of tests, how you create this stream is up to you.

The aim is to give control and power back to the writer of the tests without need to learn a new syntax and conventions,
only a few functions and types with the core of the library being simple and lightweight.

## Using

Add this dependency to your project:

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-core</artifactId>
    <version>${javatest.version}</version>
</dependency>
```

### Simple Test Definitions

The most basic `Test` can be defined by:
- A name
- A `Supplier` of an `Assertion` - a check with a description 

Functions to help you define your tests are available from the `TestProvider` interface e.g.
```java
public class MyTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(test("Simple Test", () -> that(true, "Expected test to pass")));
    }
}
```

They are also directly available as static imports from the main `JavaTest` class:

```java
import static org.javatest.JavaTest.*;

public class MyClass {
    private Stream<Test> tests = Stream.of(
            test("Simple Test", () -> that(true, "Expected test to pass"))
          );
}
```

### Creating And Composing Assertions

Assertions are created simply from boolean expressions and a string description.

```java
public class MyTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Expected one add one to be two")),
                test("Multiplication", () -> {
                    var two = 2;
                    var ten = 10;
                    var twenty = 20;
                    var expected = String.format("Expected %d times %d to be %d", two, ten, twenty);
                    return that(two * ten == twenty, expected);
                }),
                test("Palindrome", () -> {
                    var word = "Level";
                    return that(MyObject.isPalindrome(word), "Expected " + word + " to be a palindrome");
                })
            );
    }
}
```

Assertions can be composed using the `and`, `or` and `xor` default methods. These are all examples of composed assertions
that hold (i.e. will pass tests):

```java
var orAssertion = that(1 + 1 == 3, "Expected one add one to be three")
    .or(that(2 + 2 == 4, "Expected two add two to be four")) 
var andAssertion = that(1 + 1 == 2, "Expected one add one to be two").and(orAssertion)
  
that(true, "Expected to hold").xor(that(false, "Expected not to hold"))
````

### Composing Test Providers

If you split your tests across multiple `TestProvider`s you can easily combine them as such:

```java
public class AllMyTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return allTestsFrom(new MyFirstTests(), new MySecondTests()); 
    }
}
```

### Pending org.javatest.eventually.Tests

Sometimes it will be useful to define a bunch of test cases ahead of implementing them, this is where
pending tests come in. They will not fail your build but will logged in a different colour than passing/failing tests.
You can optionally provide a reason this test has not yet been written.

```java
public class MyTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
            test("Addition", () -> that(1 + 1 == 2, "Expected one add one to be two")),
            test("Multiplication", () -> pending()),
            test("Division by Zero",
                () -> pending("I am not yet sure if this should throw an exception or return a failure value"))
        );
    }
}
```

### Tagging org.javatest.eventually.Tests

Tagging tests is quite common to define subsets of tests, you can pass a `Collection` of `String` tags to any test. 
Running all tests with a certain tag is then as simple as:

```java

// In a provider
test("My special test", () -> that(true, "Expected to pass"), List.of("special"))


public class MySpecialTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return allTestsFrom(new AllMyTests())
            .filter(test -> test.tags().contains("special")); 
    }
}
```

## Running JavaTest

To run javatest simply pass your `Stream` of `Test`s or a `TestProvider` to the `JavaTest.run()` function and handle the
result how you see fit: 

```java
import static org.javatest.JavaTest.*;

class MyTests {
    public static void main(String... args) {
        var resultsFromProvider =  JavaTest.run(new AllMyTests());
        var directResults = run(Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Expected one add one to be two")),
                test("String lower case", () -> 
                    that("HELLO".toLowerCase().equals("hello"), "Expected lowercase 'HELLO' to be 'hello'"))
        ));
        if(resultsFromProvider.succeeded && directResults.succeeded) {
            System.out.println("Yay tests passed! :)");
        } else {
            throw new RuntimeException("Boo tests failed! :(");
        }
    }
}
```

### With JavaFire Maven plugin

If you are using maven you can add the `JavaFire` maven plugin to your pom to run tests defined by a `TestProvider` for you
during mavens `test` phase:

```xml
<plugin>
    <groupId>org.javatest</groupId>
    <artifactId>javafire-maven-plugin</artifactId>
    <version>${javatest.version}</version>
    <configuration>
        <testProvider>your.class.Here</testProvider>
    </configuration>
    <executions>
        <execution>
            <id>test</id>
            <goals>
                <goal>test</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Matchers

The core of JavaTest is deliberately very simple, there is a separate module that contains more expressive `Matcher` driven
`Assertion`s. You can include this module with this dependency:

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-matchers</artifactId>
    <version>${javatest.version}</version>
</dependency>
```

You will now have the ability to use a `MatcherTestProvider` that comes with a few default matchers, other matchers will
be able to be mixed into your test class by adding different interfaces e.g. `StringMatchers` which contains `String` specific
`Matcher`s.

```java
public class MyTests implements MatcherTestProvider, StringMatchers {
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

### Exception Matching

While in general I prefer error values to exceptions, I understand there are those that disagree or just have to work with
exception driven Java APIs so I have added matcher syntax for exceptions. These should still be returned as an assertion and will
ensure that the test will only pass if the matched exception is thrown from the expected code block.

```java
public class MyExceptionTests implements MatcherTestProvider, ExceptionMatchers, StringMatchers {
    @Override
        public Stream<Test> testStream() {
            return Stream.of(
                    test("Simple Exception", () -> {
                        var validator = new MyValidator();
                        return that(() -> validator.validate(1), willThrowExceptionThat(hasType(IllegalArgumentException.class)));
                    }),
                    test("Check Message on cause", () -> {
                        var myObject = new MyThrowingObject;
                        var hasCauseWithMessageContainingFoo = hasCauseThat(hasMessageThat(contains("Foo")));
                        return that(() -> myObject.throwingFunction(10), willThrowExceptionThat(hasCauseWithMessageContainingFoo));
                    })
            );
        }
}

```

## Eventual Consistency

TODO: docs

## RoadMap

My plan for the first released version is to:

- [x] Write a few more common matchers e.g. for `Collection`s, `Map`s, `Optional`s and `Comparable`s.
- [ ] Eventual Consistency Module
- [ ] Create an abstraction for composite matchers.
- [ ] Figure out how I would like to handle fixtures in the API e.g. creating a database connection and passing that to tests
- [ ] Decide how to handle null. At the moment many `that(null, $matcher)` expressions fail tests with NPEs, maybe this is good enough?
Maybe I should explicitly fail if null is passed?
- [ ] Ensure I am happy with the level of simplicity in the core library.

Future Versions could include:

- A module that allows for generative property testing & parameterised testing more generally.
- A way to add arbitrary logs to your test cases.
- The ability to select how different parts of your test stream is run e.g. some in sequence the rest in parallel.


## Rationale

(A.K.A. Why even bother?)

While there is nothing wrong with existing test frameworks, I think the language and engineering practices
have come a long way since their conception, my hope is this framework will help to leverage these changes in a few ways.

1.  Current frameworks tend to be **Annotation** and **Exception** driven, this framework aims to be
**Function** and **Value** driven. The idea is that these tests will be more composable and easier to reason about.

2. _If you know how to write a Java application, I think you should automatically know
how to write the tests_.A lot of test frameworks require you to learn a new syntax and require your test code to be written
in a completely different style from your production code, `JavaTest` tests are written in pure functional java. 
 
3. A test should ideally only test one thing and should certainly test _at least_ one thing. Since
all tests must return an assertion value the compiler will enforce both of these for you. This also has the benefit 
that you will always know where the test is failing (the last line) and don't need to create and wrangle stack traces
in order to locate your failures.

4. Constructing your test functions no longer requires magic invocations of test classes (at the minor loss of having
to declare where your tests are). This means injecting parameters into your tests becomes easier and can be checked 
at compile time instead of runtime. For example the parameterised tests in JUnit requiring runtime type checks on 
arguments and sometimes a CSV parser to take statically defined values and assign types to them, this can just be 
replaces with typed parameter generation for your test functions. It will also be easier to reason about the flow of
tests and parameters.
