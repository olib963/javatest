package io.github.olib963.javatest.fixtures.internal;

import io.github.olib963.javatest.*;
import io.github.olib963.javatest.fixtures.FixtureDefinition;
import io.github.olib963.javatest.javafire.AssertionResult;
import io.github.olib963.javatest.javafire.TestResults;
import io.github.olib963.javatest.javafire.TestRunner;
import io.github.olib963.javatest.javafire.fixtures.FixtureDefinition;

import java.util.Optional;
import java.util.function.Function;

// TODO are there valid use cases for the before each and after each fixtures?
public class FixtureRunner<Fixture> implements TestRunner {

    private final String fixtureName;
    private final FixtureDefinition<Fixture> fixtureDefinition;
    private final Function<Fixture, TestRunner> testFunction;

    public FixtureRunner(String fixtureName, FixtureDefinition<Fixture> fixtureDefinition, Function<Fixture, TestRunner> testFunction) {
        this.fixtureName = fixtureName;
        this.fixtureDefinition = fixtureDefinition;
        this.testFunction = testFunction;
    }

    // TODO I think this currently adds to the failure count of the test results. This is technically not correct. During refactoring
    // it would be good to rework this so the test counts remain the same, but the result fails and a log is added.
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
        return new TestResult(AssertionResult.exception(e), flattenMessages(e));
    }

    private String flattenMessages(Throwable t) {
        return Optional.ofNullable(t.getCause())
                .map(c -> t.getMessage() + System.lineSeparator() + flattenMessages(c))
                .orElseGet(t::getMessage);
    }
}
