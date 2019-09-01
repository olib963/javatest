package io.github.olib963.javatest.matchers;

import io.github.olib963.javatest.Testable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.matchers.CollectionMatchers.*;
import static io.github.olib963.javatest.matchers.Matcher.hasType;
import static io.github.olib963.javatest.matchers.Matcher.that;

public class CollectionMatcherTests {

    public static Testable.TestSuite suite() {
        return Utils.matcherSuite("Collection Matcher Tests",
                Stream.of(
                        test("Size", () -> that(List.of(1, 2), hasSize(2))),
                        test("Empty", () -> that(List.of(), isEmpty())),
                        test("Contains", () -> that(List.of(1, 2, 3), contains(2))),
                        test("Contains All", () -> that(List.of(1, 2, 3, 4, 5), containsAll(1, 3, 5))),
                        test("Contains That", () -> that(List.of(1, "hello", true), containsElementThat(hasType(String.class))))
                ),
                Stream.of(
                        test("Size (FAIL - bigger)", () -> that(List.of(1, 2), hasSize(1))),
                        test("Size (FAIL - smaller)", () -> that(List.of(1, 2), hasSize(3))),
                        test("Empty (FAIL)", () -> that(List.of(1), isEmpty())),
                        test("Contains (FAIL)", () -> that(List.of(1, 2, 3), contains(4))),
                        test("Contains All (FAIL - none)", () -> that(List.of(1, 2, 3, 4, 5), containsAll(6, 7, 8))),
                        test("Contains All (FAIL - some)", () -> that(List.of(1, 2, 3, 4, 5), containsAll(1, 3, 7))),
                        test("Contains That (FAIL)", () -> that(List.of(1, "hello", true), containsElementThat(hasType(Collection.class))))
                )
        );
    }
}
