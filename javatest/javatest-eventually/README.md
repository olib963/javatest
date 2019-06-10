# Eventual Consistency

Sometimes you will need to write an assertion that you cannot guarantee will hold straight away. You can use the `Eventually`
static imports to write tests that will eventually hold.

```java
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.eventually.Eventually.*;

public class MyEventualTest implements TestSuite {
    
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
which covers one minute. You can pass an instance of `EventualConfig` to the `eventually` function to customise the behaviour:

```java
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.eventually.Eventually.*;
import static io.github.olib963.javatest.eventually.EventualConfig;

public class MyCustomEventualTest implements TestSuite {
    
    // Simple assertion just used to demonstrate how to call the eventually function
    private Assertion assertion(){
        return that(true, "This should pass"); 
    }
    
    // Configuration of 3 attempts, waiting 2 seconds between each with an initial delay of 3 seconds.
    private EventualConfig myConfig = EventualConfig.of(3, Duration.ofSeconds(2), Duration.ofSeconds(3));
    
    @Override
    public Stream<Test> testStream() {
        return Stream.of(
          // Using default config of 13 attempts and 5 second wait with no initial delay
          test("A", () -> eventually(this::assertion)),
          // Using our custom config      
          test("B", () -> eventually(this::assertion, myConfig)),
          // Using no initial delay but keeping 3 attempts and 2 second wait      
          test("C", () -> eventually(this::assertion, myConfig.withNoInitialDelay())),
          // 10 Attempts with 50 millis between each, keeping the 3 second initial delay
          test("D", () -> eventually(this::assertion, myConfig.withAttempts(10).withWaitInterval(Duration.ofMillis(50))))
        );
    }
}
```

_______

You can include this module with this dependency: 

```xml
<dependency>
    <groupId>io.github.olib963</groupId>
    <artifactId>javatest-eventually</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```
