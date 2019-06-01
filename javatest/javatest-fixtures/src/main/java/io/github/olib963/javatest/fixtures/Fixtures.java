package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.CheckedSupplier;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.fixtures.internal.FixtureRunner;
import io.github.olib963.javatest.fixtures.internal.FunctionFixtureDefinition;
import io.github.olib963.javatest.fixtures.internal.TemporaryDirectory;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.olib963.javatest.fixtures.Try.Success;
import static io.github.olib963.javatest.fixtures.Try.Try;

public class Fixtures {
    private Fixtures() {
    }

    public static <Fixture> TestRunner fixtureRunner(String fixtureName,
                                                     FixtureDefinition<Fixture> fixtureDefinition,
                                                     Function<Fixture, TestRunner> testFunction) {
        return new FixtureRunner<>(fixtureName, fixtureDefinition, testFunction);
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromFunction(Supplier<Try<Fixture>> creator) {
        return definitionFromFunctions(creator, x -> Success());
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromFunctions(Supplier<Try<Fixture>> creator, Function<Fixture, Try<Void>> destroyer) {
        return new FunctionFixtureDefinition<>(creator, destroyer);
    }

    // Helper function to allow you to define a fixture using an existing, exception driven java API
    public static <Fixture> FixtureDefinition<Fixture> definitionFromThrowingFunction(CheckedSupplier<Fixture> creator) {
        return definitionFromThrowingFunctions(creator, f -> {});
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromThrowingFunctions(CheckedSupplier<Fixture> creator, CheckedConsumer<Fixture> destroyer) {
        return definitionFromFunctions(() -> Try(creator), (Fixture f) -> Try(destroyer, f));
    }

    public static FixtureDefinition<File> temporaryDirectory(String path) {
        return new TemporaryDirectory(path);
    }

}
