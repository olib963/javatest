package org.javatest.fixtures.internal;

import org.javatest.*;
import org.javatest.fixtures.FixtureDefinition;

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
        try {
            var fixture = fixtureDefinition.create();
            return runWithFixture(fixture);
        } catch (Exception error) {
            var failed = AssertionResult.exception(error);
            var result = new TestResult(failed, "Could not create fixture \"" + fixtureName + "\"\n" + failed.description);
            return TestResults.init().addResult(result);
        }
    }

    private TestResults runWithFixture(Fixture fixture) {
        var results = testFunction.apply(fixture).run();
        try {
            fixtureDefinition.destroy(fixture);
            return results;
        } catch (Exception error) {
            var failed = AssertionResult.exception(error);
            var result = new TestResult(failed, "Could not destroy fixture \"" + fixtureName + "\"\n" + failed.description);
            return results.addResult(result);
        }
    }
}
