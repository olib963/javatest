package org.javatest.fixtures;

import org.javatest.*;
import org.javatest.fixtures.internal.FixtureRunner;
import org.javatest.fixtures.internal.FunctionFixture;
import org.javatest.fixtures.internal.TemporaryDirectory;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;

public class Fixtures {
    private Fixtures() {
    }

    static <F> TestRunner fixtureRunner(String fixtureName,
                                        Fixture<F> fixture,
                                        Function<F, Stream<Test>> testFunction) {
        return fixtureRunnerWrapper(fixtureName, fixture, f -> JavaTest.testStreamRunner(testFunction.apply(f)));
    }

    static <F> TestRunner fixtureRunnerFromProvider(String fixtureName,
                                                    Fixture<F> fixture,
                                                    Function<F, TestProvider> testFunction) {
        return fixtureRunnerWrapper(fixtureName, fixture, f -> JavaTest.testStreamRunner(testFunction.apply(f)));
    }

    static <F> TestRunner fixtureRunnerWrapper(String fixtureName,
                                               Fixture<F> fixture,
                                               Function<F, TestRunner> testFunction) {
        return new FixtureRunner<>(fixtureName, fixture, testFunction);
    }

    static <F> Fixture<F> fromFunction(CheckedSupplier<F> creator) {
        return fromFunctions(creator, x -> {});
    }

    static <F> Fixture<F> fromFunctions(CheckedSupplier<F> creator, CheckedConsumer<F> destroyer) {
        return new FunctionFixture<>(creator, destroyer);
    }

    static Fixture<File> temporaryDirectory(String path) {
        return new TemporaryDirectory(path);
    }

}
