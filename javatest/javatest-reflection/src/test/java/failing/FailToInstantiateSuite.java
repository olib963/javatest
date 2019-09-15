package failing;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;

public class FailToInstantiateSuite implements TestSuiteClass {

    public FailToInstantiateSuite() {
        throw new RuntimeException("Cannot instantiate");
    }

    @Override
    public Collection<Testable> testables() {
        return List.of(
                test("Passing test", () -> that(true, "passing"))
        );
    }
}
