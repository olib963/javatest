package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.RunConfiguration;
import io.github.olib963.javatest.TestCompletionObserver;

public class RunConfigurations {

    // Configuration that logs individual tests in colour and the full results of the run
    public RunConfiguration defaultConfig = RunConfiguration.defaultConfig();

    // Configuration that logs individual tests with no colour and logs a warning
    // if there are any pending tests at all in the run.
    public RunConfiguration customConfig = RunConfiguration.empty()
            .addTestObserver(TestCompletionObserver.plainLogger())
            .addRunObserver(results -> {
                if (results.pendingCount != 0) {
                    System.err.println("!!You still have unwritten tests!!");
                }
            });
}
// end::include[]
