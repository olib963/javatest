package org.javatest.matchers;

import org.javatest.Test;
import org.javatest.TestProvider;

import java.util.List;
import java.util.stream.Stream;

public class StringMatcherTests implements TestProvider, StringMatchers {

    private final List<String> tags = List.of("string-matchers");

    public Stream<Test> passingTests = Stream.of(
            test("String Prefix", () -> that("Hello World", startsWith("Hello")), tags),
            test("String Suffix", () -> that("Hello World", endsWith("World")), tags)
    );

    public Stream<Test> failingTests = Stream.of(
            test("String Prefix (FAIL)", () -> that("Hello World", startsWith("World")), tags),
            test("String Suffix (FAIL)", () -> that("Hello World", endsWith("Hello")), tags)
    );
}
