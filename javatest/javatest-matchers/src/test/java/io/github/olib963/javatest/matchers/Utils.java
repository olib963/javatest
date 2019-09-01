package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable.*;

import java.util.Collections;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Utils {

    public static TestSuite matcherSuite(String suiteName, Stream<Test> passingTests, Stream<Test> failingTests) {
        return suite(suiteName,Stream.of(
                suite("Passing Tests", passingTests),
                suite("Failing Tests", failingTests.map(t ->
                    test(t.name, () -> {
                        var results = run(Stream.of(testableRunner(t)), Collections.emptyList());
                        return that(!results.succeeded, t.name + " should fail");
                    })))
        ));
    }
}
