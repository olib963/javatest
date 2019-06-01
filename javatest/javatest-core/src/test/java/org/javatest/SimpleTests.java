package org.javatest;

import java.util.stream.Stream;

import static org.javatest.JavaTest.*;

public class SimpleTests {

    public static Testable passing() {
        return new PassingTests();
    }

    static class PassingTests implements TestSuite {

        @Override
        public Stream<Test> tests() {
            return Stream.of(
                    test("Simple test", () -> that(true, "Expected true to be true")),
                    test("Pending test that has yet to be written", JavaTest::pending),
                    test("Pending test that has yet to be written", () -> pending("That has a name")),
                    test("And test", () -> that(true, "Expected true").and(that(true, "Expected true"))),
                    test("Pending test and 1", () -> that(true, "Expected true").and(pending())),
                    test("Pending test and 2", () -> pending().and(that(true, "Expected true"))),
                    test("Pending test and with failing", () -> pending().and(that(false, "Expected false"))),
                    test("Or test 1", () -> that(true, "Expected true").or(that(true, "Expected true"))),
                    test("Or test 2", () -> that(false, "Expected false").or(that(true, "Expected true"))),
                    test("Or test 3", () -> that(true, "Expected true").or(that(false, "Expected false"))),
                    test("Pending test or 1", () -> that(true, "Expected true").or(pending())),
                    test("Pending test or 2", () -> pending().or(that(true, "Expected true"))),
                    test("Pending test or with failing", () -> pending().or(that(false, "Expected false"))),
                    test("Xor test 1", () -> that(false, "Expected false").xor(that(true, "Expected true"))),
                    test("Xor test 2", () -> that(true, "Expected true").xor(that(false, "Expected false"))),
                    test("Pending test xor 1", () -> that(true, "Expected true").xor(pending())),
                    test("Pending test xor 2", () -> pending().xor(that(true, "Expected true")))
            );
        }
    }

    public static Stream<Test> FAILING = Stream.of(
            test("Simple test (FAIL)", () -> that(false, "Expected false to be true")),
            test("And test 1 (FAIL)", () -> that(false, "Expected false").and(that(false, "Expected false"))),
            test("And test 2 (FAIL)", () -> that(true, "Expected true").and(that(false, "Expected false"))),
            test("And test 3 (FAIL)", () -> that(false, "Expected false").and(that(true, "Expected true"))),
            test("Or test (FAIL)", () -> that(false, "Expected false").or(that(false, "Expected false"))),
            test("Xor test 1 (FAIL)", () -> that(true, "Expected true").xor(that(true, "Expected true"))),
            test("Xor test 2 (FAIL)", () -> that(false, "Expected false").xor(that(false, "Expected false"))),
            test("SimpleTest throwing exception (FAIL)", () -> { throw new RuntimeException("This is an error"); }),
            test("SimpleTest throwing checked exception (FAIL)", () -> { throw new Exception("This is an error"); }),
            test("SimpleTest throwing assertion error (FAIL)", () -> { throw new AssertionError("This is an 'assertion'"); })
    );
}
