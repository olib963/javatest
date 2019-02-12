package org.javatest.tests;

import org.javatest.assertions.Assertion;
import org.javatest.JavaTest;

import java.util.Collection;
import java.util.stream.Stream;

public interface TestProvider {
    Stream<Test> testStream();

    default Test test(String name, CheckedSupplier<Assertion> test) {
        return JavaTest.test(name, test);
    }

    default Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return JavaTest.test(name, test, tags);
    }

    default Assertion that(boolean asserted, String description) { return JavaTest.that(asserted, description); }

    default Assertion pending() {
        return JavaTest.pending();
    }

    default Assertion pending(String reason) {
        return JavaTest.pending(reason);
    }

    default Stream<Test> allTestsFrom(TestProvider... providers) {
        return JavaTest.allTestsFrom(providers);
    }
}
