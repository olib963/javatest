package org.javatest;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class JavaTest {

    public static boolean run(Stream<Test> tests) {
        return tests.allMatch(s -> s.success);
    }

    public static Test test(String description, Supplier<Boolean> test){
        return new Test(description, test.get());
    }

    public static <A> Function<Function<A, Stream<Test>>, Stream<Test>> before(Supplier<A> beforeAction) {
        return tests -> tests.apply(beforeAction.get());
    }

    public static <A> Function<Stream<Function<A, Test>>, Stream<Test>> beforeEach(Supplier<A> beforeAction) {
        return tests -> tests.map(t -> t.apply(beforeAction.get()));
    }
}
