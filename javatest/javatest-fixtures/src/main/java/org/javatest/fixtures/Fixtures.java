package org.javatest.fixtures;

import org.javatest.*;
import org.javatest.fixtures.internal.FixtureRunner;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Fixtures {

    default <F> TestRunner fixtureRunner(String fixtureName,
                                               CheckedSupplier<F> creator,
                                               CheckedConsumer<F> destroyer,
                                               Function<F, Stream<Test>> testFunction) {
        return fixtureRunnerWrapper(fixtureName, creator, destroyer, f -> JavaTest.testStreamRunner(testFunction.apply(f)));
    }

    default <F> TestRunner fixtureRunnerFromProvider(String fixtureName,
                                                           CheckedSupplier<F> creator,
                                                           CheckedConsumer<F> destroyer,
                                                           Function<F, TestProvider> testFunction) {
        return fixtureRunnerWrapper(fixtureName, creator, destroyer, f -> JavaTest.testStreamRunner(testFunction.apply(f)));
    }

    default <F> TestRunner fixtureRunnerWrapper(String fixtureName,
                                                      CheckedSupplier<F> creator,
                                                      CheckedConsumer<F> destroyer,
                                                      Function<F, TestRunner> testFunction) {
        return Runners.fixtureRunnerWrapper(fixtureName, creator, destroyer, testFunction);
    }

    class Runners {
        private Runners(){}
        static <F> TestRunner fixtureRunner(String fixtureName,
                                             CheckedSupplier<F> creator,
                                             CheckedConsumer<F> destroyer,
                                             Function<F, Stream<Test>> testFunction) {
            return fixtureRunnerWrapper(fixtureName, creator, destroyer, f -> JavaTest.testStreamRunner(testFunction.apply(f)));
        }

        static <F> TestRunner fixtureRunnerFromProvider(String fixtureName,
                                                         CheckedSupplier<F> creator,
                                                         CheckedConsumer<F> destroyer,
                                                         Function<F, TestProvider> testFunction) {
            return fixtureRunnerWrapper(fixtureName, creator, destroyer, f -> JavaTest.testStreamRunner(testFunction.apply(f)));
        }

        static <F> TestRunner fixtureRunnerWrapper(String fixtureName,
                                                    CheckedSupplier<F> creator,
                                                    CheckedConsumer<F> destroyer,
                                                    Function<F, TestRunner> testFunction) {
            return new FixtureRunner<>(fixtureName, creator, destroyer, testFunction);
        }
    }

}
