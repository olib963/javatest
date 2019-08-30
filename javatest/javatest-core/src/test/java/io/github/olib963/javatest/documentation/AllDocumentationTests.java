package io.github.olib963.javatest.documentation;

import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;

public class AllDocumentationTests implements TestSuiteClass {
    @Override
    public Stream<Testable> testables() {
        var assertions = new MyAssertions();
        var composed = new MyComposedAssertions();
        return Stream.of(
                test("Assertion definitions should hold", () -> assertions.simpleAssertion.and(assertions.multilineFormattedAssertion())),
                new MyTests().myFirstTest,
                test("Composed or assertion holds", () -> composed.orAssertion),
                test("Composed and assertion holds", () -> composed.andAssertion),
                test("Composed xor assertion holds", () -> composed.xorAssertion)
        );
    }
}
