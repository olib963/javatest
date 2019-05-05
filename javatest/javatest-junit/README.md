# JUnit Runner

It is simple to run JUnit tests as part of your JavaTest run by using the `JUnitRunner`. This runner makes use of
the [programmatic launcher API](https://junit.org/junit5/docs/current/user-guide/#launcher-api) to run JUnit tests. You
create any arbitrary `LauncherDiscoveryRequest` to call out to JUnit and there is a convenience method
that exists for just running all tests in a package.

```java
public class RunningJUnit {
    
    public static void main(String... args) {
        var fromPackage = JUnitTestRunner.fromPackage("foo.bar");
        LauncherDiscoveryRequest customRequest = createCustomRequest();
        var customRunner = JUnitTestRunner.fromRequest(customRequest);
        var result = JavaTest.run(fromPackage, customRunner);
        if(!result.succeeded) {
            throw new IllegalStateException("OH NO! JUnit tests failed");
        } else {
            System.out.println("Yay JUnit tests still pass");
        }
    }
}
```

_______

You can include this module with this dependency: 

```xml
<dependency>
    <groupId>org.javatest</groupId>
    <artifactId>javatest-junit</artifactId>
    <version>${javatest.version}</version>
    <scope>test</scope>
</dependency>
```

## TODO

- [ ] Collect the logs where appropriate and add them to the `TestResults` object.
- [ ] Find out what (if any) extensibility of the launcher is required to run JUnit tests your way.