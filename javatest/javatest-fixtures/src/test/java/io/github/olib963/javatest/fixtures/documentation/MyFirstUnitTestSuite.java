package io.github.olib963.javatest.fixtures.documentation;
// tag::include[]
import io.github.olib963.javatest.TestSuiteClass;
import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.CollectionMatchers.contains;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.StringMatchers.containsString;

public class MyFirstUnitTestSuite implements TestSuiteClass {
    @Override
    public Collection<Testable> testables() {
        return List.of(
                test("List contains", () -> that(List.of(1,2,3), contains(2))),
                test("Messaging", () -> {
                    var myObject = new MyBusinessMessageObject();
                    var message = myObject.createMessageFor(50);
                    return that(message, containsString("integer 50"));
                })
        );
    }
}
// end::include[]
