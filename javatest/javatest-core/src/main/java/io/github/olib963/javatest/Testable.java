package io.github.olib963.javatest;


import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

// Tagging interface for the Test | TestSuite ADT. This should not be implemented externally except through TestSuiteClass
public interface Testable {

    <A> A match(Function<TestSuite, A> suiteFn, Function<Test, A> testFn);

    final class Test implements Testable {
        public final String name;
        public final CheckedSupplier<Assertion> test;
        Test(String name, CheckedSupplier<Assertion> test) {
            this.name = name;
            this.test = test;
        }

        @Override
        public <A> A match(Function<TestSuite, A> suiteFn, Function<Test, A> testFn) {
            return testFn.apply(this);
        }
    }

    final class TestSuite implements Testable {
        public final String name;
        private final Collection<? extends Testable> testables;
        TestSuite(String name, Collection<? extends Testable> testables) {
            this.name = name;
            this.testables = testables;
        }

        public Stream<? extends Testable> testables() {
            return testables.stream();
        }

        @Override
        public <A> A match(Function<TestSuite, A> suiteFn, Function<Test, A> testFn) {
            return suiteFn.apply(this);
        }
    }
}
