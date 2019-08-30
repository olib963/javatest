package io.github.olib963.javatest.eventually.documentation;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;
import io.github.olib963.javatest.eventually.EventualConfig;

import java.time.Duration;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
// tag::imports[]
import static io.github.olib963.javatest.JavaTest.that;
import static io.github.olib963.javatest.eventually.Eventually.eventually;
// end::imports[]

public class ConfigDocumentationTests implements TestSuiteClass {

    // tag::config[]
    public class MyCustomEventualAssertions {
        // Configuration of 3 attempts, waiting 2 seconds between each with an initial delay of 3 seconds.
        private EventualConfig myConfig = EventualConfig.of(3, Duration.ofSeconds(2), Duration.ofSeconds(3));

        private Assertion attempt20TimesAndWait100Millis = eventually(this::assertion);

        private Assertion attemptUsingCustomConfig = eventually(this::assertion, myConfig);

        private Assertion attemptUsingCustomConfigNoDelay = eventually(this::assertion, myConfig.withNoInitialDelay());

        private Assertion attemptUsingCustomConfigWithOverrides =
                eventually(this::assertion, myConfig.withAttempts(10).withWaitInterval(Duration.ofMillis(50)));

        // Simple assertion just used to demonstrate how to call the eventually function
        private Assertion assertion() {
            return that(true, "This should pass");
        }
    }
    // end::config[]

    @Override
    public Stream<Testable> testables() {
        var container = new MyCustomEventualAssertions();
        return Stream.of(
                test("Default config should work", () -> container.attempt20TimesAndWait100Millis),
                test("Custom config should work", () -> container.attemptUsingCustomConfig),
                test("Custom config (no delay) should work", () -> container.attemptUsingCustomConfigNoDelay),
                test("Custom config (overrides) should work", () -> container.attemptUsingCustomConfigWithOverrides)
        );
    }
}
