package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.*;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class MyPendingTests implements TestSuite {
    @Override
    public Stream<Test> tests() {
        return Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Expected one add one to be two")),
                test("Multiplication", () -> pending()),
                test("Division by Zero",
                        () -> pending("I am not yet sure if this should throw an exception or return a failure value"))
        );
    }
}
// end::include[]
