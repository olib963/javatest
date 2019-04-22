package org.javatest.junit;

import org.javatest.TestResults;
import org.javatest.TestRunner;

import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class JUnitTestRunner implements TestRunner {

    private final LauncherDiscoveryRequest request;

    public JUnitTestRunner(LauncherDiscoveryRequest request) {
        this.request = request;
    }

    @Override
    public TestResults run() {
        var launcher = LauncherFactory.create();
        var listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        var summary = listener.getSummary();

        var failures = summary.getTestsFailedCount();
        var successes = summary.getTestsSucceededCount();

        // TODO we can print failure logs to a string writer, but we cannot seem to print success logs.
        // See if people want to get the logs into the test results object.
        return TestResults.from(failures, successes);
    }

    public static TestRunner fromPackage(String packageName) {
        return new JUnitTestRunner(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage(packageName))
                .build());
    }
}
