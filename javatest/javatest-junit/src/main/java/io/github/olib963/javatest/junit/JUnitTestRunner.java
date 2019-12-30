package io.github.olib963.javatest.junit;

import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.jupiter.engine.JupiterTestEngine;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;


public final class JUnitTestRunner implements TestRunner {
    private static final TestEngine ENGINE = new JupiterTestEngine();
    private final LauncherDiscoveryRequest request;

    private JUnitTestRunner(LauncherDiscoveryRequest request) {
        this.request = request;
    }

    @Override
    public TestResults run() {
        var launcher = LauncherFactory.create(LauncherConfig.builder().addTestEngines(ENGINE).build());
        var listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        var summary = listener.getSummary();

        var failures = summary.getTestsFailedCount();
        var successes = summary.getTestsSucceededCount();

        return TestResults.from(failures, successes);
    }

    public static TestRunner fromRequest(LauncherDiscoveryRequest request) {
        return new JUnitTestRunner(request);
    }

    public static TestRunner fromPackage(String packageName) {
        return new JUnitTestRunner(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage(packageName))
                .build());
    }
}
