package io.github.olib963.javatest.documentation.functions;

// This class is not tested, just compiled
// tag::include[]
import io.github.olib963.javatest.TestCompletionObserver;
import io.github.olib963.javatest.TestRunCompletionObserver;

public class FunctionalObservers {
    // Log a message to yourself to remind you that you have still have tests to write
    public TestRunCompletionObserver personalWarning = result -> {
        if (result.pendingCount != 0) {
            System.out.println("\n\n\n!!You still have unwritten tests!!\n\n\n");
        }
    };

    // Replace logging with simple "X completed" log
    public TestCompletionObserver simpleLog = result -> {
        var log = result.match(
                suiteResult -> suiteResult.suiteName + "completed",
                singleTestResult -> singleTestResult.name + "completed"
        );
        System.out.println(log);
    };
}
// end::include[]
