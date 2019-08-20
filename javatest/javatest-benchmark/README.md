# Benchmarking

To add a timer to your tests or runners you need to wrap them in a benchmarker. This will add the time taken
to the logs of your test.

```java
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.benchmark.Benchmark.*;

public class MyBenchMarks {
    
    private Test test = /* create some JavaTest test */
    public Test timedTest = benchmark(test);
    
    // Add benchmarking to each test
    public Stream<Testable> allTestsTimed = benchmarkAllTests(
            Stream.of(
               new MyFirstSuite(), new MySecondSuite(), test     
            ));
    
}
```

## Setting time limits

You can set a time limit on an individual test such that it will fail if it takes any longer

```java
import java.time.Duration;
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.benchmark.Benchmark.*;

public class MyLimitedTest {
    
    private Test longTest = test("Some long test", () -> {
       Thread.sleep(5000l);
       return that(true, "Ordinarily this would pass.");
    });
    
    public Test failingTest = benckmark(longTest, Duration.ofSeconds(4));
}
```

## Custom duration format

By default test `Duration`s will be logged as `{seconds}s {millis}ms`. You can override
this function on any benchmarking by providing your own format.

```java
import java.time.Duration;
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.benchmark.Benchmark.*;

public class MyCustomBenchmark {
    
    private Function<Duration, String> format = d -> d.toMillis() + "milliseconds";
    
    private Test test = /* create some JavaTest test */
    public Test timedTest = benchmark(test, format);
    
}
```
_______

You can include this module with this dependency:

```xml
<dependency>
    <groupId>io.github.olib963</groupId>
    <artifactId>javatest-benchmarking</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```
