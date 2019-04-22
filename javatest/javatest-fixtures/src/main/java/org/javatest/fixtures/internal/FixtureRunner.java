package org.javatest.fixtures.internal;

import org.javatest.*;
import org.javatest.fixtures.Fixture;

import java.util.function.Function;

// TODO are there valid use cases for the before each and after each fixtures?
public class FixtureRunner<FixtureType> implements TestRunner {

    private final String fixtureName;
    private final Fixture<FixtureType> fixture;
    private final Function<FixtureType, TestRunner> testFunction;

    public FixtureRunner(String fixtureName, Fixture<FixtureType> fixture, Function<FixtureType, TestRunner> testFunction) {
        this.fixtureName = fixtureName;
        this.fixture = fixture;
        this.testFunction = testFunction;
    }

    // TODO I think this currently adds to the failure count of the test results. This is technically not correct. During refactoring
    // it would be good to rework this so the test counts remain the same, but the result fails and a log is added.
    @Override
    public TestResults run() {
        try {
            var f = fixture.create();
            return runWithFixture(f);
        } catch (Throwable throwable) {
            var failed = AssertionResult.exception(throwable);
            var result = new TestResult(failed, "Could not create fixture \"" + fixtureName + "\"\n" + failed.description);
            return TestResults.init().addResult(result);
        }
    }

    private TestResults runWithFixture(FixtureType f) {
        var results = testFunction.apply(f).run();
        try {
            fixture.destroy(f);
            return results;
        } catch (Throwable throwable) {
            var failed = AssertionResult.exception(throwable);
            var result = new TestResult(failed, "Could not destroy fixture \"" + fixtureName + "\"\n" + failed.description);
            return results.addResult(result);
        }
    }
}
