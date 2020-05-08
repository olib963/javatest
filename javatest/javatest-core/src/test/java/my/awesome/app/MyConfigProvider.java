package my.awesome.app;

import io.github.olib963.javatest.RunConfiguration;
import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestResult;
import io.github.olib963.javatest.TestRunCompletionObserver;
import io.github.olib963.javatest.javafire.RunConfigurationProvider;

/**
 * My config provider only logs failed tests and the full run results
 */
public class MyConfigProvider implements RunConfigurationProvider {

    @Override
    public RunConfiguration config() {
        var loggingObserver = TestCompletionObserver.colourLogger();
        TestCompletionObserver onlyLogFailures = result -> {
            if(testFailed(result)) {
                loggingObserver.onTestCompletion(result);
            }
        };
        return RunConfiguration.empty()
                .addRunObserver(TestRunCompletionObserver.logger())
                .addTestObserver(onlyLogFailures);
    }

    private boolean testFailed(TestResult result) {
        return result.match(
                suiteResult -> suiteResult.results().anyMatch(this::testFailed),
                singleTestResult -> !singleTestResult.result.holds
        );
    }
}
