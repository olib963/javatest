package io.github.olib963.javatest;

import io.github.olib963.javatest.assertions.AllAssertion;
import io.github.olib963.javatest.assertions.BooleanAssertion;
import io.github.olib963.javatest.assertions.PendingAssertion;
import io.github.olib963.javatest.runners.CollectionRunner;
import io.github.olib963.javatest.runners.StreamRunner;

import java.util.*;
import java.util.stream.Stream;

public class JavaTest {
    private JavaTest() {}

    // Main entry point of the library
    public static TestResults run(Collection<TestRunner> runners) {
        return run(runners, RunConfiguration.defaultConfig());
    }

    public static TestResults run(Collection<TestRunner> runners, RunConfiguration config) {
        var results = runners.stream().map(r -> r.run(config)).reduce(TestResults.empty(), TestResults::combine);
        config.runObservers().forEach(o -> o.onRunCompletion(results));
        return results;
    }

    // Convenience functions
    public static TestResults run(TestRunner firstRunner, TestRunner... moreRunners) {
        var runners = new ArrayList<TestRunner>(moreRunners.length + 1);
        runners.add(firstRunner);
        runners.addAll(Arrays.asList(moreRunners));
        return run(runners);
    }

    public static TestResults runTests(Testable tests) {
        return runTests(List.of(tests));
    }
    public static TestResults runTests(Testable firstTest, Testable... tests) {
        var testables = new ArrayList<Testable>(tests.length + 1);
        testables.add(firstTest);
        testables.addAll(Arrays.asList(tests));
        return runTests(testables);
    }

    public static TestResults runTests(Collection<? extends Testable> tests) {
        return run(testableRunner(tests));
    }

    // Stream Runner factory methods
    public static TestRunner testableRunner(Testable testable) {
        return testableRunner(List.of(testable));
    }

    public static TestRunner testableRunner(Collection<? extends Testable> tests) {
        return new CollectionRunner(tests);
    }

    public static TestRunner lazyTestableRunner(Stream<? extends Testable> tests) {
        return new StreamRunner(tests);
    }

    // Test factory methods
    public static Testable.Test test(String name, CheckedSupplier<Assertion> testFunction) {
        return new Testable.Test(name, testFunction);
    }

    public static Testable.TestSuite suite(String name, Collection<? extends Testable> testables) {
        return new Testable.TestSuite(name, testables);
    }

    public static Assertion that(boolean asserted, String description) {
        return new BooleanAssertion(asserted, description);
    }

    public static Assertion pending() {
        return pending("Test has not yet been written");
    }

    public static Assertion all(Assertion firstAssertion, Assertion... moreAssertions) {
        List<Assertion> list = new ArrayList<>();
        list.add(firstAssertion);
        list.addAll(Arrays.asList(moreAssertions));
        return all(list);
    }

    public static Assertion all(Collection<Assertion> assertions) {
        return new AllAssertion(assertions);
    }

    public static Assertion pending(String reason) {
        return new PendingAssertion(reason);
    }

}

