# JavaTest

[![CircleCI](https://circleci.com/gh/olib963/javatest.svg?style=svg)](https://circleci.com/gh/olib963/javatest)

| Fair Warning: The entirety of this project should currently be considered alpha and subject to change. I am sure I have not got the API or library right on the first try so there may be breaking changes in the future. |
| --- |

## Overview

An opinionated, functional test framework written in pure Java aiming to leverage newer features of the Java language and give
power back to the test writer.

* If you know how to write a Java application, I think you should automatically know how to write the tests, 
therefore JavaTest only introduces a few new functions and types to the language, there is no new syntax.

* Instead of being **Annotation** and **Exception** driven, JavaTest is **Function** and **Value** driven,
 lending itself to composability of tests and assertions.
 
* JavaTests contains no Magic. A more declarative approach to testing is taken where setting up and customising tests is left
to the writer. 
 
* A test should ideally only test one thing and should certainly test _at least_ one thing. Since
all tests must return an assertion value the compiler will enforce that all tests only test one result.

Tests should be easy to understand and enjoyable to write, after all we all spend a lot of our time working on them :D

## Quick Start

### I'm new to Java
<details>
<summary>Expand</summary>

Download the latest jar artifact of JavaTest Core. Then create these files:

1. foo/Calculator.java

This is the System Under Test representing the source code for your application (in this case a calculator that can add integers)

```java
package foo;

public class Calculator {
    public static int add(int a, int b) {
        return a + b; 
    }
}
```

2. foo/Tests.java

This file contains tests for our SUT, it exists in the same package so there is no need to 
`import foo.Calculator;`. 

This example defines two simple tests, one is testing that `1 + 1 = 2` by
simply using the java `+` function and the other test checks our calculator gets the same result. We then
invoke the `runTests` function to run our tests and check if they passed.


```java
package foo;

import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = runTests(Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Math still works, one add one is still two")),
                test("Calculator Addition", () -> that(Calculator.add(1, 1) == 2, "My math is correct, one add one is still two"))));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}
```

You can then run from the commandline:

```bash
# Compile both Java classes ensuring JavaTest and the current directory are both on the class path
javac -cp "/absolute/path/to/javatest/jar:." foo/Calculator.java foo/Tests.java

# Run the "Tests" executable ensuring JavaTest and the current directory are both on the class path
java -cp "/absolute/path/to/javatest/jar:." foo.Tests
```

Notes:
* You will need to use `;` to separate classpath entries instead of `:` on windows machines
* You will need to include at least the Javatest jar and the current directory (`.`) on the classpath in order for this to work,
if you are using java classes from any other jars/directories you will need to also ensure they are on the classpath.

You should be able to explore the core library and get familiar with testing your code very quickly by running them from 
an executable.

</details>

### I know Java pretty well

<details>
<summary>Expand</summary>

</details>

## Contents

* [Overview](#overview)
* [Quick Start](#quick-start)
* [The Core](#core-library)
* [Running JavaTest](#running-javatest)
* [Roadmap](#roadmap)

### Module List

JavaTest is built on a simple functional core and functionality is expanded on by several modules found here:

* [Matchers](./javatest/javatest-matchers)
* [Fixtures](./javatest/javatest-fixtures)
* [Parameterised Testing](./javatest/javatest-parameterised)
* [Eventual Consistency](./javatest/javatest-eventually)
* [JUnit](./javatest/javatest-junit)

## Core Library

- `JavaTest`: the entrypoint class. It contains the main `run` function as well as factory functions
- `TestRunner`: returns `TestResults`, the only core implementation being `StreamRunner`
- `TestSuite`: logical collection of a stream of `Test`s
- `Test`: a named instance of a test, each test must return an `Assertion`
- `Assertion`: represents the expected state at the end of a test
- `TestResult`: represents the result of a single test
- `TestResults`: represents the combined result of multiple tests

### Simple Test Definitions

The most basic `Test` can be defined by:
- A name
- A `Supplier` of an `Assertion` - a check with a description 

Functions to help you define your tests are available by statically importing them from `JavaTest` e.g.
```java
import static org.javatest.JavaTest.*;

public class MyTests {
    Test myFirstTest = test("Simple Test", () -> that(true, "Expected test to pass"));
}
```

### Creating And Composing Assertions

Assertions are created simply from boolean expressions and a string description.

```java
import static org.javatest.JavaTest.*;

public class MyAssertions {
    Assertion simpleAssertion = that(1 + 1 == 2, "Expected one add one to be two");
    
    public Assertion multilineFormattedAssertion() {
        var two = 2;
        var ten = 10;
        var twenty = 20;
        var expected = String.format("Expected %d times %d to be %d", two, ten, twenty);
        return that(two * ten == twenty, expected); 
    }
}
```

Assertions can be composed using the `and`, `or` and `xor` default methods. These are all examples of composed assertions
that hold (i.e. will pass tests):

```java
import static org.javatest.JavaTest.*;

class MyComposedAssertions { 
    Assertion orAssertion = that(1 + 1 == 3, "Expected one add one to be three")
        .or(that(2 + 2 == 4, "Expected two add two to be four"));
         
    Assertion andAssertion = that(1 + 1 == 2, "Expected one add one to be two").and(orAssertion);
  
    Assertion xorAssertion = that(true, "Expected to hold").xor(that(false, "Expected not to hold"));

}
````

### Test Suites

You can group your tests into logical units using `TestSuite`s

```java
import static org.javatest.JavaTest.*;

public class MyFirstTests implements TestSuite {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(test("Simple Test", () -> that(true, "Expected test to pass")));
    }
}
```


If you split your tests across multiple `TestSuite`s you can easily combine them as such:

```java
import static org.javatest.JavaTest.*;

public class AllMyTests implements TestSuite {
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
import static org.javatest.JavaTest.*;

public class MyTests implements TestSuite {
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
import static org.javatest.JavaTest.*;

public class AllMyTests implements TestSuite {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("My special test", () -> that(true, "Expected to pass"), List.of("special"))
                // .... more tests ...
        );
    }
}

public class MySpecialTests implements TestSuite {
    @Override
    public Stream<Test> testStream() {
        return allTestsFrom(new AllMyTests())
            .filter(test -> test.tags().contains("special")); 
    }
}
```

### Core library maven dependency

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
result how you see fit. There is a convenience function `runTests` defined to just run a `Stream<Test>` using the default `StreamRunner`: 

```java
import static org.javatest.JavaTest.*;

class MyTests {
    public static void main(String... args) {
        var results = runTests(Stream.of(
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

If you are using maven you can add the `JavaFire` maven plugin to your pom to run tests defined by a `TestRunners` class for you
during mavens `test` phase. Your `TestRunners` class _must_ have a zero arg constructor.

```java
package my.awesome.app;

import static org.javatest.JavaTest.*;

public class MyTests implements TestRunners {
    @Override
    public Stream<TestRunner> runners() {
        var unitTests = testStreamRunner(allTestsFrom(/* list of suites */).parallel());
        var applicationTests = Fixtures.fixtureRunner(
                "database connection",
                 MyFixtures.connectToDb(), 
                 db -> testStreamRunner(new MyIntegrationTests(db).testStream()));
        return Stream.of(unitTests, applicationTests);
    }
}
```

In `pom.xml`:
 
```xml
<plugin>
    <groupId>org.javatest</groupId>
    <artifactId>javafire-maven-plugin</artifactId>
    <version>${javatest.version}</version>
    <configuration>
        <testRunners>my.awesome.app.MyTests</testRunners>
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

### JShell

Since JavaTest is built on pure Java it plays quite nicely with the REPL. This startup script may be useful:
```jshelllanguage
/env -class-path /absolute/path/to/javatest/jar
import static org.javatest.JavaTest.*;

org.javatest.TestResults runTest(org.javatest.CheckedSupplier<org.javatest.Assertion> testFn) {
    return runTests(Stream.of(test("JShell test", testFn)));
}
```

Then you can run:

```bash
~$ jshell --startup DEFAULT --startup /path/to/startup/script
|  Welcome to JShell -- Version 11.0.1
|  For an introduction type: /help intro

jshell> var results = runTest(() -> that(true, "JavaTest works in the shell!"))
JShell test
	JavaTest works in the shell!

Ran a total of 1 tests.
1 succeeded
0 failed
0 were pending

results ==> org.javatest.TestResults@4b553d26

jshell> var results2 = runTest(() -> that(1 + 1 == 2, "Addition is working"))
JShell test
	Addition is working

Ran a total of 1 tests.
1 succeeded
0 failed
0 were pending

results2 ==> org.javatest.TestResults@3e6fa38a

jshell> results.succeeded && results2.succeeded
$3 ==> true
```

## Roadmap

My plan for the first released version is to:

- [x] Create a core library with a simple, functional API.
- [x] Create a simple maven plugin to run JavaTest as a helper.
- [x] Write a few common matchers e.g. for `Collection`s, `Map`s, `Optional`s, `Comparable`s etc. in a matcher module.
- [x] Create a module to allow eventually consistent assertions.
- [x] Create a module to allow the creation of test fixtures e.g. temp directories and DB connections.
- [x] Create a module to allow you to run JUnit tests within JavaTest.
- [x] Create a module to allow parameterised testing. 
- [x] Decide how (or even if) to handle null values. E.g. someone returning `Stream.of(null)` or `() -> null` for an assertion. 
**I decided to not handle `null` values at all for now**.
- [x] Separate the side effects into test observers that can be excluded. Initially this is just a logger to print
each result.
- [x] Decide on which approach to take for the API: Mixins or static imports. **I have decided for most cases static imports
are the most flexible with optional mixins where it is beneficial e.g. `Eventually`.**
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

## Feedback

Any feedback/constructive criticism is appreciated. Please open an issue if you have any suggestions
