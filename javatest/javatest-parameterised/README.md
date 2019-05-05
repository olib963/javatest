# Parameterised Testing

It can be useful to create multiple tests that have the same structure but differ only in the input values. This can be 
done by providing a stream of inputs and a function from the type of that input to a `Test`.

```java
import static org.javatest.parameterised.Parameterised.*;

public class MyPalindromeTests implements TestProvider {
    
    @Override
    public Stream<Test> testStream() {
        return parameterised(
                Stream.of("Civic", "Deified", "Kayak", "Level", "Madam"),
                word -> test("Palindrome Test for " + word, () ->
                    that(MyClass.isPalindrome(word), word + " should be a palindrome")));
    }
}

// Or if you need larger data sets/longer test methods and want to clean up the code:
public class MyLargeTest implements TestProvider {
    Stream<String> palindromes = Files.lines(Path.of("/my-palindromes.txt"));
    
    @Override
    public Stream<Test> testStream() {
        return parameterised(palindromes, this::palindromeTest);
    }
    
    private Test palindromeTest(String word) {
        return test("Palindrome Test for " + word, () -> {
            var myClass = new MyClass();
            return that(myClass.isPalindrome(word), word + " should be a palindrome"); 
        }); 
    }
}
```

If you need multiple parameters you can make use of some helper `Tuple` classes that run from `Tuple2` to `Tuple10`
and provide a function with the correct arity and typed arguments to create your tests.

```java
import static org.javatest.parameterised.Parameterised.*;

public class FibonacciTests implements TestProvider, Parameterised {
    
    @Override
    public Stream<Test> testStream() {
        return parameterised(
                Stream.of(
                        // t() is the tuple constructor and can be applied to up to 10 arguments
                        t(0, 0L),
                        t(1, 1L),
                        t(2, 1L),
                        t(3, 2L),
                        t(4, 3L),
                        t(5, 5L),
                        t(6, 8L),
                        t(8, 21L),
                        t(10, 55L),
                        t(60, 1548008755920L),
                        t(90, 2880067194370816120L)),
                        // We then create a function with the same arity and types as our tuples
                        (n, fib) -> test(n + "th fibonacci number", () ->
                            that(MyFibs.fibonacci(n) == fib,
                             "The " + n + "th fibonacci number is " + fib)));
    }
}
```

_______

You can include this module with this dependency: 

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-parameterised</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```
