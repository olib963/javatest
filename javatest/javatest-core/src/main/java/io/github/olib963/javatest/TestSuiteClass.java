package io.github.olib963.javatest;


import java.util.Collection;
import java.util.function.Function;

import static io.github.olib963.javatest.JavaTest.suite;

public interface TestSuiteClass extends Testable {

    @Override
    default <A> A match(Function<TestSuite, A> suiteFn, Function<Test, A> testFn){
        return suiteFn.apply(suite(getClass().getSimpleName(), testables()));
    }

    Collection<Testable> testables();
}
