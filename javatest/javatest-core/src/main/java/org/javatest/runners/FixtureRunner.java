package org.javatest.runners;

import org.javatest.*;

import java.util.function.Function;

// TODO part of core? Or separate module and keep core just being the API?
// TODO are there valid use cases for the before each and after each fixtures?
public class FixtureRunner<F> implements TestRunner {

    private final String fixtureName;
    private final CheckedSupplier<F> creator;
    private final CheckedConsumer<F> destroyer;
    private final Function<F, TestRunner> testFunction;

    public FixtureRunner(String fixtureName, CheckedSupplier<F> creator, CheckedConsumer<F> destroyer, Function<F, TestRunner> testFunction) {
        this.fixtureName = fixtureName;
        this.creator = creator;
        this.destroyer = destroyer;
        this.testFunction = testFunction;
    }

    // TODO I think this currently adds to the failure count of the test results. This is technically not correct. During refactoring
    // it would be good to rework this so the test counts remain the same, but the result fails and a log is added.
    @Override
    public TestResults run() {
        try {
            var fixture = creator.get();
            return runWithFixture(fixture);
        } catch (Throwable throwable) {
            var failed = AssertionResult.failed(throwable);
            var result = new TestResult(failed, "Could not create fixture \"" + fixtureName + "\"\n" + failed.description);
            return TestResults.init().addResult(result);
        }
    }

    private TestResults runWithFixture(F fixture) {
        var results = testFunction.apply(fixture).run();
        try {
            destroyer.accept(fixture);
            return results;
        } catch (Throwable throwable) {
            var failed = AssertionResult.failed(throwable);
            var result = new TestResult(failed, "Could not destroy fixture \"" + fixtureName + "\"\n" + failed.description);
            return results.addResult(result);
        }
    }
}
