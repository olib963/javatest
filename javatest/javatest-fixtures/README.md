# Fixtures

Some tests (usually integration tests) will require external resources in order to run. You can use a 
`FixtureDefinition<Fixture>` with a `FixtureRunner<Fixture>` to run such tests. For example:

```java
public class MyIntegrationTests implements TestProvider {
    private final File tmpDir;
    public MyIntegrationTests(File tmpDir) { this.tmpDir = tmpDir; }
    
    @Override
    public Stream<Test> testStream() {
        // Create some tests that write to files and/or directories in tmpDir
        return Stream.of(
                ...
        )
    }
}


public class MyEntryPoint {
    
    public static void main(String... args) {
        var runner = Fixtures.fixtureRunnerFromProvider(
            // Name the fixture
            "test directory",
            // Use the existing fixture definition for temporary directories
            Fixtures.temporaryDirectory("integraton-test"),
            // Create your test provider using the fixture
            IntegrationTests::new);
        
        var results = JavaTest.run(runner);
        ...
    }
}
```

## Custom Fixture Definitions

You can create your own `FixtureDefinition`s by extending the interface or for simple cases using the convenience functions
to create definitions from functions:

```java

public class CustomDefinitions {

    FixtureDefinition<Map<String, String>> hashmapDefinition =
        Fixtures.fromFunctions(() -> new HashMap<>(), m -> m.clear());
    
    // If you only need to create the fixture and no clean up is needed:
    FixtureDefinition<String> stringDefinition = 
        Fixtures.fromFunction(() -> "test string");
    
    // Throw exceptions (for now) if the create/tear down fail
    FixtureDefinition<Integer> stringDefinition = 
            Fixtures.fromFunction(() -> { throw new IllegalStateException("Oh dear something went wrong!"); });

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

## TODO

- [ ] If there are any common test fixtures create the required functions.
- [ ] Decide how to handle failures, should we create a `Try[A]` control structure?
