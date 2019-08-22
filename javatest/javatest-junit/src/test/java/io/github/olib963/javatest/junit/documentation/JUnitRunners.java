package io.github.olib963.javatest.junit.documentation;

import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.junit.JUnitTestRunner;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.util.List;

// tag::junitRunners[]
public class JUnitRunners {

    public static List<TestRunner> junitRunners() {
        var fromPackage = JUnitTestRunner.fromPackage("foo.bar");
        LauncherDiscoveryRequest customRequest = createCustomRequest();
        var customRunner = JUnitTestRunner.fromRequest(customRequest);
        return List.of(fromPackage, customRunner);
    }

    private static LauncherDiscoveryRequest createCustomRequest() {
        return LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        DiscoverySelectors.selectPackage("bar.baz"),
                        DiscoverySelectors.selectMethod("foo.bar.MyJUnitTest", "testFoo"))
                .filters(ClassNameFilter.excludeClassNamePatterns("^(.*Failing.*)$"))
                .configurationParameter("foo", "bar")
                .build();
    }

}
// end::junitRunners[]
