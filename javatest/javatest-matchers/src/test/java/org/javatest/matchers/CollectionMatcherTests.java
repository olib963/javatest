package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestSuite;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;
import static org.javatest.matchers.Matcher.*;
import static org.javatest.matchers.CollectionMatchers.*;

public class CollectionMatcherTests {
    private static final List<String> tags = List.of("collection-matchers");

    public static TestSuite passing() {
        return new PassingTests();
    }

    public static TestSuite failing() {
        return new FailingTests();
    }

    static class PassingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Size", () -> that(List.of(1, 2), hasSize(2)), tags),
                    test("Empty", () -> that(List.of(), isEmpty()), tags),
                    test("Contains", () -> that(List.of(1, 2, 3), contains(2)), tags),
                    test("Contains All", () -> that(List.of(1, 2, 3, 4, 5), containsAll(1, 3, 5)), tags),
                    test("Contains That", () -> that(List.of(1, "hello", true), containsElementThat(hasType(String.class))), tags)
            );
        }
    }

    static class FailingTests implements TestSuite {
        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Size (FAIL - bigger)", () -> that(List.of(1, 2), hasSize(1)), tags),
                    test("Size (FAIL - smaller)", () -> that(List.of(1, 2), hasSize(3)), tags),
                    test("Empty (FAIL)", () -> that(List.of(1), isEmpty()), tags),
                    test("Contains (FAIL)", () -> that(List.of(1, 2, 3), contains(4)), tags),
                    test("Contains All (FAIL - none)", () -> that(List.of(1, 2, 3, 4, 5), containsAll(6, 7, 8)), tags),
                    test("Contains All (FAIL - some)", () -> that(List.of(1, 2, 3, 4, 5), containsAll(1, 3, 7)), tags),
                    test("Contains That (FAIL)", () -> that(List.of(1, "hello", true), containsElementThat(hasType(Collection.class))), tags)
            );
        }
    }

}
