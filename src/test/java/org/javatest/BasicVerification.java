package org.javatest;

import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class BasicVerification {

    public static void main(String... args) {
        assert JavaTest.run(
                before(() -> "Hello")
                        .apply(string ->
                                Stream.concat(
                                        beforeEach(() -> 7)
                                                .apply(Stream.of(
                                                        x -> test("String is correct", () -> string.equals("Hello")),
                                                        integer -> test("Integer is correct", () -> integer == 7))),
                                        before(() -> "World")
                                                .apply(newString ->
                                                        Stream.of(test("Both Strings are correct", () -> (string + newString).equals("HelloWorld")))))));
    }
}
