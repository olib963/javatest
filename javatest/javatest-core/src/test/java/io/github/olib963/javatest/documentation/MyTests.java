package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.testable.Test;
import static io.github.olib963.javatest.JavaTest.*;

public class MyTests {
    Test myFirstTest = test("Simple Test", () -> that(true, "Expected test to pass"));
}
// end::include[]
