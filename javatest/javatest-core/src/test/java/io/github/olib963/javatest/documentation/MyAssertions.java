package io.github.olib963.javatest.documentation;

// tag::include[]
import io.github.olib963.javatest.Assertion;
import static io.github.olib963.javatest.JavaTest.that;

public class MyAssertions {
    Assertion simpleAssertion = that(1 + 1 == 2, "Expected one add one to be two");

    public Assertion multilineFormattedAssertion() {
        var two = 2;
        var ten = 10;
        var twenty = 20;
        var expected = String.format("Expected %d times %d to be %d", two, ten, twenty);
        return that(two * ten == twenty, expected);
    }
}
// end::include[]
