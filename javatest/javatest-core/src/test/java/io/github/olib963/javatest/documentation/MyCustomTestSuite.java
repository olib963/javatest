package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.stream.Stream;

public class MyCustomTestSuite implements TestSuite {
    @Override
    public String name() {
        return "MySuite";
    }

    @Override
    public Stream<Test> tests() {
        return Stream.of(
                // create tests
        );
    }
}
// end::include[]
