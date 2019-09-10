package io.github.olib963.javatest.matchers;
import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = runTests(List.of(
                SimpleMatcherTests.suite(),
                StringMatcherTests.suite(),
                ExceptionMatcherTests.suite(),
                OptionalMatcherTests.suite(),
                ComparableMatcherTests.suite(),
                CollectionMatcherTests.suite(),
                MapMatcherTests.suite()
        ));
        if(!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }

        var docResult = run(testableRunner(new DocumentationTests()));
        if(!docResult.succeeded) {
            throw new RuntimeException("Documentation tests failed!");
        }
    }

}
