package org.javatest;

import java.util.List;

public class BasicVerification {

    public static void main(String... args) {
        assert JavaTest.run(List.of(
                () -> new Test("test", true)
        ));

        assert !JavaTest.run(List.of(
                () -> new Test("test", true),
                () -> new Test("test", false)
        ));
    }
}
