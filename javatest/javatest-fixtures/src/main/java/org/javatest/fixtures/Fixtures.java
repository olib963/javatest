package org.javatest.fixtures;

import org.javatest.CheckedSupplier;
import org.javatest.TestRunner;
import org.javatest.fixtures.internal.FixtureRunner;
import org.javatest.fixtures.internal.FunctionFixtureDefinition;
import org.javatest.fixtures.internal.TemporaryDirectory;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.javatest.fixtures.Try.*;

public class Fixtures {
    private Fixtures() {
    }

    public static <Fixture> TestRunner fixtureRunner(String fixtureName,
                                               FixtureDefinition<Fixture> fixtureDefinition,
                                               Function<Fixture, TestRunner> testFunction) {
        return new FixtureRunner<>(fixtureName, fixtureDefinition, testFunction);
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromFunction(CheckedSupplier<Fixture> creator) {
        return definitionFromFunctions(creator, x -> {});
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromFunctions(CheckedSupplier<Fixture> creator, CheckedConsumer<Fixture> destroyer) {
        return definitionFromFunctions(() -> Try(creator), (Fixture f) -> Try(destroyer, f));
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromFunction(Supplier<Try<Fixture>> creator) {
        return definitionFromFunctions(creator, x -> Success());
    }

    public static <Fixture> FixtureDefinition<Fixture> definitionFromFunctions(Supplier<Try<Fixture>> creator, Function<Fixture, Try<Void>> destroyer) {
        return new FunctionFixtureDefinition<>(creator, destroyer);
    }

    public static FixtureDefinition<File> temporaryDirectory(String path) {
        return new TemporaryDirectory(path);
    }

}
