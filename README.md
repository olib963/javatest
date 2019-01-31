# JavaTest

Experimental attempt at a different style of test framework. Very much a work in progress. Any feedback/constructive criticism
is appreciated. I am sure I have not got the API or library right on the first try so there may be breaking changes in the future.


## Basic Principles

- Tests should be written in plain, simple functional Java with no magic.
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
- A description
- A `Supplier` of an `Assertion`

Functions to help you define your tests are available from the `TestProvider` interface e.g.
```java
public class MyTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(test("One add one is two", () -> that(1 + 1 == 2)));
    }
}
```

### Creating And Composing Assertions

Assertions are created simply from boolean expressions with the option to add a description to what it is you are testing.

```java
public class MyTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Addition", () -> that(1 + 1 == 2)),
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

Assertions can be composed using the `and`, `or` and `xor` default methods. e.g.

```
that(1 + 1 == 2).and(
  that(1 + 1 == 3).or(that(2 + 2 == 4)))
  
that(true).xor(that(false))
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
            test("One add one is two", () -> that(1 + 1 == 2)),
            test("Two times ten is twenty", () -> pending()),
            test("One divided by zero throws exception",
                () -> pending("I am not yet sure if this should throw an exception or return a failure value"))
        );
    }
}
```

### Tagging Tests

Tagging tests is quite common to define subsets of tests, you can pass a `Collection` of `String` tags to any test:
`test("My special test", () -> that(true), List.of("special"))`

Running all tests with a certain tag is then as simple as:

```java
public class MySpecialTests implements TestProvider {
    @Override
    public Stream<Test> testStream() {
        return allTestsFrom(new AllMyTests())
            .filter(test -> test.tags.contains("special")); 
    }
}
```

## Running JavaTest

To run javatest simply pass your `Stream` of `Test`s or a `TestProvider` to the `JavaTest.run()` function and handle the
result how you see fit: 

```java
class MyTests {
    public static void main(String... args) {
        var resultsFromProvider =  JavaTest.run(new AllMyTests());
        var directResults = JavaTest.run(Stream.of(
                test("Addition", () -> that(1 + 1 == 2)),
                test("String lower case", () -> that("HELLO".toLowerCase().equals("hello")))
        ));
        if(resultsFromProvider.succeeded && directResults.succeeded) {
            System.out.println("Yay tests passed!");
        } else {
            throw new RuntimeException("Tests failed!");
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
be able to be mixed into your test class by implementing a different interface e.g. `StringMatchers` which contains `String` specific
`Matcher`s.

```java
public class MyTests implements MatcherTestProvider, StringMatchers {
    private static final String TEST_STRING = "Hello World";
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("One add One is Two", () -> that(1 + 1, isEqualTo(2))),
                test("Object is the correct type", () -> that("Hello", hasType(String.class))),
                test("String has prefix", () -> that(TEST_STRING, startsWith("Hello"))),
                test("String contains substring", () -> that(TEST_STRING, containsString("llo")))
        );
    }
}
```

## RoadMap

My plan for the first released version is to:

- [ ] Write a few more common matchers e.g. for `Collection`s, `Map`s, `Optional`s and `Comparable`s.
- [ ] Figure out how I would like to handle fixtures in the API e.g. creating a database connection and passing that to tests
- [ ] Figure out if I would rather use the java module system rather than a multi-module maven project (I have no java module
experience yet so haven't looked into it hence starting with maven modules).
- [ ] Ensure I am happy with the log output of `Assertion`s e.g. composing with `and`.
- [ ] Ensure I am happy with the level of simplicity in the core library.

Future Versions could include:

- A module to test eventual consistency for example something like: `thatEventually(() -> that(map.containsKey(1)), 1, Minutes, 10, Seconds)`
would retry `map.containsKey(1)` every 10 seconds until 1 minute has passed.
- A module that allows for generative property testing & parameterised testing more generally.
