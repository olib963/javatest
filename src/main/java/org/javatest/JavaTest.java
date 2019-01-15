package org.javatest;

import java.util.Collection;
import java.util.function.Supplier;

public class JavaTest {

    public static boolean run(Collection<Supplier<Test>> tests) {
        return tests.stream().allMatch(s -> s.get().success);
    }
}
