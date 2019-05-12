# Fixtures

Some tests (usually integration tests) will require external resources in order to run. You can use a 
`FixtureDefinition<Fixture>` with a `FixtureRunner<Fixture>` to run such tests. For example:

```java
public class MyIntegrationTests implements TestSuite {
    private final File tmpDir;
    public MyIntegrationTests(File tmpDir) { this.tmpDir = tmpDir; }
    
    @Override
    public Stream<Test> testStream() {
        // Create some tests that write to files and/or directories in tmpDir
        return Stream.of(
                //...
        );
    }
}


public class MyEntryPoint {
    
    public static void main(String... args) {
        var runner = Fixtures.fixtureRunner(
            // Name the fixture
            "test directory",
            // Use the existing fixture definition for temporary directories
            Fixtures.temporaryDirectory("integration-test"),
            // Create your test runner using the fixture
            d -> testStreamRunner(new IntegrationTests(d).testStream()));
        
        var results = JavaTest.run(runner);
    }
}
```

## Custom Fixture Definitions

You can create your own `FixtureDefinition`s by extending the interface or for simple cases using the convenience functions
to create definitions from functions. You can make use of the `org.javatest.fixtures.Try` static factories to
create `Success` and `Failure` results for your definition.

```java

import static org.javatest.fixtures.Try.*;

public class CustomDefinitions {
    

    // If you only need to create the fixture and no clean up is needed:
    FixtureDefinition<String> stringDefinition =
            Fixtures.definitionFromFunction(() -> Success("Hello"));
    
    // Express failures as a string
    FixtureDefinition<Integer> intDefinition = 
            Fixtures.definitionFromFunction(() -> Failure("Oh dear something went wrong!"));
    
    // Add a tear down function
    FixtureDefinition<Map<String, String>> hashMapDefinition =
            Fixtures.definitionFromFunctions(
                    () -> Success(new HashMap<>()), 
                    map -> { 
                        map.clear();
                        return Success(); 
                    });
    
    // Equivalent helpers are available that can wrap existing java functions that throw exceptions
    FixtureDefinition<ExecutorService> singleThreadExecutorDefinition =
                Fixtures.definitionFromThrowingFunctions(Executors::newSingleThreadExecutor, ExecutorService::shutdown);

}


```
_______

You can include this module with this dependency: 

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-fixtures</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```
