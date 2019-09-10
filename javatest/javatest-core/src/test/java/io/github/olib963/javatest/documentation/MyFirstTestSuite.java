package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.Testable.TestSuite;

import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;

public class MyFirstTestSuite {

    public static TestSuite mySuite() {
        return suite("MyTests", List.of(
                test("Simple Test", () -> that(true, "Expected test to pass"))
        ));
    }
}
// end::include[]
