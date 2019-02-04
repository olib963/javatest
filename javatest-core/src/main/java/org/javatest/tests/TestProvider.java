package org.javatest.tests;

import org.javatest.assertions.Assertion;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface TestProvider {

    Stream<Test> testStream();

    default Test test(String name, CheckedSupplier<Assertion> test) {
        return Test.test(name, test, Collections.emptyList());
    }

    default Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return Test.test(name, test, tags);
    }

    default Assertion that(boolean asserted, String description) { return Assertion.that(asserted, description); }

    default Assertion pending() {
        return Assertion.pending();
    }

    default Assertion pending(String reason) {
        return Assertion.pending(reason);
    }

    default Stream<Test> allTestsFrom(TestProvider... providers) {
        return Arrays.stream(providers).flatMap(TestProvider::testStream);
    }
}
