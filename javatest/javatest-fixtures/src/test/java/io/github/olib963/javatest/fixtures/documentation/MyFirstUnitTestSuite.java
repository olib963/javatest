package io.github.olib963.javatest.fixtures.documentation;
// tag::include[]
import io.github.olib963.javatest.Test;
import io.github.olib963.javatest.TestSuite;

import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.Matcher.that;
import static io.github.olib963.javatest.matchers.CollectionMatchers.*;
import static io.github.olib963.javatest.matchers.StringMatchers.*;

public class MyFirstUnitTestSuite implements TestSuite {
    @Override
    public Stream<Test> tests() {
        return Stream.of(
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
