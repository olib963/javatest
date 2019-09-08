package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.*;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class MyEntrypoint {
    public static void main(String... args) {
        var results = runTests(Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Expected one add one to be two")),
                test("String lower case", () ->
                        that("HELLO".toLowerCase().equals("hello"), "Expected lowercase 'HELLO' to be 'hello'"))
        ));

        var customResults = run(new MyCustomRunner());
        if(results.succeeded && customResults.succeeded) {
            System.out.println("Yay tests passed! :)");
        } else {
            throw new RuntimeException("Boo tests failed! :(");
        }
    }
}
// end::include[]

class MyCustomRunner implements TestRunner {

    @Override
    public TestResults run() {
        return TestResults.empty();
    }
}