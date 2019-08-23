package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.Testable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

// tag::definitions[]
import static io.github.olib963.javatest.fixtures.Try.*;

public class CustomDefinitions {

    // If you only need to create the fixture and no clean up is needed:
    FixtureDefinition<String> stringDefinition =
            Fixtures.definitionFromFunction(() -> Success("Hello"));

    // Express failures as a string when the fixture cannot be created
    FixtureDefinition<Integer> intDefinition =
            Fixtures.definitionFromFunction(() -> Failure("Oh dear something went wrong!"));

    // Add a tear down function to your fixture (also with possible failure)
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

    // end::definitions[]

    public static Stream<Testable> tests() {
        var container = new CustomDefinitions();
        return Stream.of(
                test("String fixture works", () ->
                        that(container.stringDefinition.create().equals(Success("Hello")),
                                "String should create a successful 'Hello'")),
                test("Int fixture fails correctly", () ->
                        container.intDefinition.create()
                            .map(i -> that(false, "Expected an error but successfully got " + i))
                            .recoverWith(e ->
                                    that(e.getMessage().equals("Oh dear something went wrong!"), "Error has correct message"))),
                test("Map fixture should tear down", () -> {
                    var map = container.hashMapDefinition.create();
                    var testingIsEmpty = map.flatMap(m -> {
                        m.put("foo", "bar"); // Put a value in the map
                        return container.hashMapDefinition.destroy(m).map(Void ->
                            that(m.isEmpty(), "Map (" + m + ") is empty")
                        );
                    });
                    return testingIsEmpty.recoverWith(e -> that(false, "Got error " + e));
                })
        );
    }
}
