package io.github.olib963.javatest;


import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.suite;

public interface TestSuiteClass extends Testable {

    @Override
    default <A> A match(Function<Test, A> testFn, Function<TestSuite, A> suiteFn){
        return suiteFn.apply(suite(getClass().getSimpleName(), testables()));
    }

    Stream<Testable> testables();
}
