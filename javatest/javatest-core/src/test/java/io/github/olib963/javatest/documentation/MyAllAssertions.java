package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.Assertion;
import static io.github.olib963.javatest.JavaTest.*;

public class MyAllAssertions {
    Assertion allAssertion = all(
            that(1 + 1 == 2, "Expected one add one to be two"),
            that(2 + 2 == 4, "Expected two add two to be four"),
            that(3 + 3 == 6, "Expected three add three to be six"),
            that(4 + 4 == 8, "Expected four add four to be eight"),
            that(5 + 5 == 10, "Expected five add five to be ten")
    );
}
// end::include[]
