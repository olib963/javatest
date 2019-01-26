# JavaTest

Experimental attempt at a different style of test framework.

## Basic Principles

- Tests should be written in plain, simple functional Java with no magic. (I may add some magic reflection at a later date if it
helps breaking tests apart, but only if it is needed.)
- Each test should return one assertion.
- JavaTest will run a stream of tests, how you create this stream is up to you. The aim is to give control and power back
 to the writer of the tests without need to learn a new syntax.

## Simple Test definition

The most basic test can be defined by:
- A description
- A `Supplier` of an `Assertion`

e.g. `Test("One add one is two", () -> that(1 + 1 == 2));`

Optionally tests can have a collection of tags applied to them: `test("Foo", () -> that(true), List.of("bar", "baz"))`

### Creating and composing assertions

The most basic assertion can be created by simply testing a boolean using `that($booleanExpression)`.

Assertions can be composed using the `and`, `or` and `xor` default methods. e.g.

```
that(1 + 1 == 2).and(
  that(1 + 1 == 3).or(that(2 + 2 == 4)))
  
that(true).xor(that(false))
````

Assertions can also be created using `Matcher`s e.g. `that(7, isEqualTo(7))`.

## Running JavaTest

To run javatest simply pass your stream of tests to the `JavaTest.run()` function: 

```java
import static org.javatest.tests.Test.*;
import static org.javatest.assertions.Assertion.*;

class MyTests {
    
    public static void main(String... args) {
        boolean passed = JavaTest.run(Stream.of(
                test("Addition", () -> that(1 + 1 == 2)),
                test("String lower case", () -> that("HELLO".toLowerCase().equals("hello")))
        )).succeeded;
    }
    
}
```

### With JavaFire Maven plugin

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