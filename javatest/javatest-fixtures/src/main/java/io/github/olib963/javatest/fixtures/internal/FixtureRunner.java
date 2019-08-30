package io.github.olib963.javatest.fixtures.internal;

import io.github.olib963.javatest.AssertionResult;
import io.github.olib963.javatest.TestResult;
import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.fixtures.FixtureDefinition;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FixtureRunner<Fixture> implements TestRunner {

    private final String fixtureName;
    private final FixtureDefinition<Fixture> fixtureDefinition;
    private final Function<Fixture, TestRunner> testFunction;

    public FixtureRunner(String fixtureName, FixtureDefinition<Fixture> fixtureDefinition, Function<Fixture, TestRunner> testFunction) {
        this.fixtureName = fixtureName;
        this.fixtureDefinition = fixtureDefinition;
        this.testFunction = testFunction;
    }

    @Override
    public TestResults run() {
        return fixtureDefinition.create()
                .mapError(e -> new Exception("Could not create fixture \"" + fixtureName + '"', e))
                .map(this::runWithFixture)
                .recoverWith(e -> TestResults.init().addResult(exceptionToResult(e)));

    }

    private TestResults runWithFixture(Fixture fixture) {
        var results = testFunction.apply(fixture).run();
        return fixtureDefinition.destroy(fixture)
                .map(unit -> results)
                .recoverWith(e -> results.addResult(
                        exceptionToResult(new Exception("Could not destroy fixture \"" + fixtureName + '"', e))));
    }

    private TestResult exceptionToResult(Exception e) {
        return new TestResult.SingleTestResult(AssertionResult.exception(e), List.of(flattenMessages(e)));
    }

    private String flattenMessages(Throwable t) {
        return Optional.ofNullable(t.getCause())
                .map(c -> t.getMessage() + System.lineSeparator() + flattenMessages(c))
                .orElseGet(t::getMessage);
    }
}
