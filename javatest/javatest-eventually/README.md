# Eventual Consistency

Sometimes you will need to write an assertion that you cannot guarantee will hold straight away. You can use the `Eventually`
interface to write tests that will eventually hold.

```java
public class MyEventualTest implements TestProvider, Eventually {
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
          test("Map is populated", () -> {
              var map = new HashMap<String, String>();
              // You can imagine this is some asynchronous task in your code base.
              executor.submit(() -> {
                  // Due to this 2 second sleep the assertion will not hold straight away.
                  Thread.sleep(2000L);
                  map.put("foo", "bar");
              });
              return eventually(() -> 
                that("bar".equals(map.get("foo")), "Map contains entry foo:bar"));
          })      
        );
    }
}
```

The `eventually` function will by default wait 5 seconds between each attempt and will attempt to run your assertion 13 times
which covers one minute. There are multiple ways to configure the number of attempts and wait duration:

```java
public class MyCustomEventualTest implements TestProvider, Eventually {
    
    // You can override the default number of attempts made
    @Override
    public int defaultAttempts() {
        return 5;
    }
    
    // You can override the default duration to wait between attempts
    @Override
    public Duration defaultDuration() {
        return Duration.ofSeconds(1);
    }
    
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
          // Attempts 5 times, waiting 1 second between each attempt      
          test("A", () -> eventually(() -> pending())),
          // Attempts 10 times, waiting 1 second between each attempt      
          test("B", () -> eventually(() -> pending(), 10)),
          // Attempts 5 times, waiting 3 seconds between each attempt      
          test("C", () -> eventually(() -> pending(), Duration.ofSeconds(3))),
          // Attempts 10 times, waiting 3 seconds between each attempt      
          test("D", () -> eventually(() -> pending(), Duration.ofSeconds(3), 10))
        );
    }
}
```

You will need the dependency: 

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-eventually</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```
