package org.javatest.fixtures;

import org.javatest.CheckedConsumer;
import org.javatest.CheckedSupplier;
import org.javatest.TestRunner;
import org.javatest.fixtures.internal.FixtureRunner;
import org.javatest.fixtures.internal.FunctionFixtureDefinition;
import org.javatest.fixtures.internal.TemporaryDirectory;

import java.io.File;
import java.util.function.Function;

final public class Fixtures {
    private Fixtures() {
    }

    static <Fixture> TestRunner fixtureRunner(String fixtureName,
                                               FixtureDefinition<Fixture> fixtureDefinition,
                                               Function<Fixture, TestRunner> testFunction) {
        return new FixtureRunner<>(fixtureName, fixtureDefinition, testFunction);
    }

    static <Fixture> FixtureDefinition<Fixture> definitionFromFunction(CheckedSupplier<Fixture> creator) {
        return definitionFromFunctions(creator, x -> {});
    }

    static <Fixture> FixtureDefinition<Fixture> definitionFromFunctions(CheckedSupplier<Fixture> creator, CheckedConsumer<Fixture> destroyer) {
        return new FunctionFixtureDefinition<>(creator, destroyer);
    }

    static FixtureDefinition<File> temporaryDirectory(String path) {
        return new TemporaryDirectory(path);
    }

}
