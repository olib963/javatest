package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.RunConfiguration;
import io.github.olib963.javatest.Testable.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Utils {

    public static TestSuite matcherSuite(String suiteName, Stream<Test> passingTests, Stream<Test> failingTests) {
        return suite(suiteName, List.of(
                suite("Passing Tests", passingTests.collect(Collectors.toList())),
                suite("Failing Tests", failingTests.map(t ->
                    test(t.name, () -> {
                        var results = run(List.of(testableRunner(t)), RunConfiguration.empty());
                        return that(!results.succeeded, t.name + " should fail");
                    })).collect(Collectors.toList()))
        ));
    }
}
