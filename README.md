# JavaTest

Experimental attempt at a different style of test framework. Very much a work in progress. Any feedback/constructive criticism
is appreciated.

| Fair Warning: The entirety of this project should currently be considered alpha and subject to change. I am sure I have not got the API or library right on the first try so there may be breaking changes in the future. |
| --- |

## Quick Start

### Java Level: Noob

### Java Level: Literally anyone else

## Contents

* [Basic Principles](#basic-principles)
* [The Core](#core-library)
* [How else can I run it?](#running-javatest)
* [But Why?](#rationale)
* [Roadmap](#roadmap)
* [Module List](#modules)

## Basic Principles

- Tests should be written in plain, simple functional Java with no magic.
- Each test should return one assertion.
- The core library will provide a way to run a `Stream` of `Test`s, however you create this `Stream` is up to you.

The aim is to give control and power back to the writer of the tests without need to learn a new syntax and conventions,
only a few functions and types with the core of the library being simple and lightweight.

## Core Library

- `JavaTest`: the entrypoint class. It contains the main `run` function aswell as factory functions
- `TestRunner`: returns `TestResults`, the only core implementation being `StreamRunner`
- `TestProvider`: provides a stream of `Test`s
- `Test`: a named instance of a test, each test must return an `Assertion`
- `Assertion`: represents the expected state at the end of a test.

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

### Pending Tests

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

### Tagging Tests

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


Core library maven dependency:

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-core</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```

## Running JavaTest

To run Javatest simply pass your `TestRunner` instances to the `JavaTest.run()` function and handle the
result how you see fit. There is a convenience function defined to just run a `Stream<Test>` using the default `StreamRunner`: 

```java
import static org.javatest.JavaTest.*;

class MyTests {
    public static void main(String... args) {
        var results = run(Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Expected one add one to be two")),
                test("String lower case", () -> 
                    that("HELLO".toLowerCase().equals("hello"), "Expected lowercase 'HELLO' to be 'hello'"))
        ));
        
        var customResults = run(new MyCustomRunner());
        if(results.succeeded && customResults.succeeded) {
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

**TODO:** change this to use `TestRunner`s instead of `TestProvider`s.


## Rationale

(A.K.A. Why even bother?)

While there is nothing wrong with existing test frameworks, I think the language (Java) and engineering practices
have come a long way since their conception, my hope is this framework will help to leverage these changes in a few ways.

1.  Current frameworks tend to be **Annotation** and **Exception** driven, this framework aims to be
**Function** and **Value** driven. The idea is that these tests will be more composable and easier to reason about, taking a more
declarative approach to testing.

2. _If you know how to write a Java application, I think you should automatically know
how to write the tests_.A lot of test frameworks require you to learn a new syntax and require your test code to be written
in a completely different style from your production code, JavaTest tests are written in pure functional java.

3. Modularity. My hope is that the core library will be very small and simple, it will provide you enough to get started 
and expose extension points to allow more complex, advanced testing in the future with each module being able to evolve
independently.
 
4. A test should ideally only test one thing and should certainly test _at least_ one thing. Since
all tests must return an assertion value the compiler will enforce both of these for you. This also has the benefit 
that you will always know where the test is failing (the last line) and don't need to create and wrangle stack traces
in order to locate your failures.

5. No Magic. Constructing your test functions no longer requires magic invocations of test classes (at the minor loss of having
to declare where your tests are). This means injecting parameters into your tests becomes easier and can be checked 
at compile time instead of runtime. For example the parameterised tests in JUnit requiring runtime type checks on 
arguments and sometimes a CSV parser to take statically defined values and assign types to them, this can just be 
replaces with typed parameter generation for your test functions. It will also be easier to reason about the flow of
tests and parameters.

6. Extensible. While this framework intends to be very opinionated on how you should write your tests, it should also allow
you to stray from its standards when appropriate e.g. running JUnit tests.

## Modules

* [Matchers](./javatest/javatest-matchers)
* [Fixtures](./javatest/javatest-fixtures)
* [But what about mah JUnits?](./javatest/javatest-junit)
* [Parameterised Testing](./javatest/javatest-pararmeterised)
* [Eventual Consistency](./javatest/javatest-eventually)

## Roadmap

My plan for the first released version is to:

- [x] Create a core library with a simple, functional API.
- [x] Create a simple maven plugin to run JavaTest as a helper.
- [x] Write a few common matchers e.g. for `Collection`s, `Map`s, `Optional`s, `Comparable`s etc. in a matcher module.
- [x] Create a module to allow eventually consistent assertions.
- [x] Create a module to allow the creation of test fixtures e.g. temp directories and DB connections.
- [x] Create a module to allow you to run JUnit tests within JavaTest.
- [x] Create a module to allow parameterised testing. 
- [ ] Decide how (or even if) to handle null values. E.g. someone returning `Stream.of(null)` or `() -> null` for an assertion.
- [ ] Decide on which approach to take for the API: Mixins or static imports.
- [ ] Separate the side effects into test observers that can be excluded. Initially this is just a logger to print
each result.
- [ ] Ensure I am happy with the level of simplicity in each module, especially the core.
- [ ] Review Documentation with people new to and familiar with Java.
- [ ] Release and get much feedbacks.

Features I would like to look at implementing in the future:

* A way to add arbitrary logs to your test cases.
* A module that allows for generative property testing & test specifications e.g. the `Comparable[T]` spec.
* Acceptance tests for the JavaFire maven plugin, this proved too complex and painful to do in the first version.
* A Gradle plugin?
* Wrappers for Scala (ScavaTest), Clojure (ClavaTest) and Kotlin (KavaTest). I feel the APIs in those languages may feel even 
more intuitive and better due to their functional nature.
* Dependant tests and/or assertions.
* Support for mocks as assertions. Pseudo example with mockito: something like 
`verifyThat(myMock).calledFunction().foo(eq("hello))`.
* TestNG Runner?
* IntelliJ Plugin if possible?
* Parallelism Options - currently achievable by using `.parallel()` on the streams but that uses the default fork join pool.
