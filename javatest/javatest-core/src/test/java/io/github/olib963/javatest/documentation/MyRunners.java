package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.TestRunner;
import java.util.Collections;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class MyRunners {

    public TestRunner singleTestRunner = testableRunner(Stream.of(
            test("Simple test", () -> pending())));

    public TestRunner suiteTestsNoLogging = testableRunner(
            Stream.of(new MyFirstTestSuite(), new MyCustomTestSuite()),
            Collections.emptyList() // No observers so no logging
    );

}
// end::include[]
