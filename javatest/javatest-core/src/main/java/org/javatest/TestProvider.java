package org.javatest;

import org.javatest.tests.SimpleTest;

import java.util.Collection;
import java.util.stream.Stream;

public interface TestProvider {
    Stream<Test> testStream();

    default Test test(String name, CheckedSupplier<Assertion> test) {
        return SimpleTest.test(name, test);
    }

    default Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return SimpleTest.test(name, test, tags);
    }

    default Assertion that(boolean asserted, String description) { return Assertion.that(asserted, description); }

    default Assertion pending() {
        return Assertion.pending();
    }

    default Assertion pending(String reason) {
        return Assertion.pending(reason);
    }

    default Stream<Test> allTestsFrom(TestProvider... providers) {
        return JavaTest.allTestsFrom(providers);
    }
}
