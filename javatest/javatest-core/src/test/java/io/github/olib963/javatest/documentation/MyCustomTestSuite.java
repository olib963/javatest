package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.stream.Stream;

public class MyCustomTestSuite implements TestSuiteClass {
//    @Override
//    public String name() {
//        return "MySuite";
//    }

    @Override
    public Stream<Testable> testables() {
        return Stream.of(
                // create testables
        );
    }
}
// end::include[]
