package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.Assertion;
import static io.github.olib963.javatest.JavaTest.*;

public class MyComposedAssertions {
    Assertion orAssertion = that(1 + 1 == 3, "Expected one add one to be three")
            .or(that(2 + 2 == 4, "Expected two add two to be four"));

    Assertion andAssertion = that(1 + 1 == 2, "Expected one add one to be two").and(orAssertion);

    Assertion xorAssertion = that(true, "Expected to hold").xor(that(false, "Expected not to hold"));
}
// end::include[]
