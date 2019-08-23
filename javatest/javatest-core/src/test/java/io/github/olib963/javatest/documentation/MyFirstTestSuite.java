package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.*;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class MyFirstTestSuite implements TestSuite {

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                test("Simple Test", () -> that(true, "Expected test to pass"))
        );
    }

}
// end::include[]
