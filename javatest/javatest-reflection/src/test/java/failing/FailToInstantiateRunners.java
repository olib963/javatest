package failing;

import io.github.olib963.javatest.TestRunner;
import io.github.olib963.javatest.javafire.TestRunners;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.*;

public class FailToInstantiateRunners implements TestRunners {

    public FailToInstantiateRunners() {
        throw new RuntimeException("Cannot instantiate");
    }

    @Override
    public Collection<TestRunner> runners() {
        return List.of(testableRunner(
                test("Passing test", () -> that(true, "passing"))
        ));
    }
}
