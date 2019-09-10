package io.github.olib963.javatest.documentation;
// tag::include[]

import io.github.olib963.javatest.Testable.TestSuite;

import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;

public class SuiteOfSuites {

    // A suite composed of one test and two suites
    public static TestSuite compositeSuite() {
        return suite("MyComposedTests",
                List.of(
                        test("Simple Test", () -> that(true, "Expected test to pass")),
                        MyFirstTestSuite.mySuite(),
                        new ClassAsSuite()
                ));
    }
}
// end::include[]
