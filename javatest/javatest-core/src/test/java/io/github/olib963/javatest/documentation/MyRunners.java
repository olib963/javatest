package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.TestRunner;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class MyRunners {

    public TestRunner singleTestRunner = testableRunner(List.of(
            test("Simple test", () -> pending())));

    public TestRunner suiteTestsNoLogging = testableRunner(
            List.of(MyFirstTestSuite.mySuite(), new ClassAsSuite()),
            Collections.emptyList() // No observers so no logging
    );

}
// end::include[]
