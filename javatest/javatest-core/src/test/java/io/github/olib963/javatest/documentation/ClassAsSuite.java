package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;

public class ClassAsSuite implements TestSuiteClass {

    @Override
    public Collection<Testable> testables() {
        return List.of(
                test("Simple Test", () -> that(true, "Expected test to pass"))
        );
    }

}
// end::include[]
