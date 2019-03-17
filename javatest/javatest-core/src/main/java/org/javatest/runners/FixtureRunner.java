package org.javatest.runners;

import org.javatest.*;

import java.util.function.Function;

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
