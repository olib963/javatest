package org.javatest.tests;

import org.javatest.assertions.Assertion;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class Test {
    public final String description;
    public final CheckedSupplier<Assertion> test;
    public final Collection<String> tags;
    Test(String description, CheckedSupplier<Assertion> test, Collection<String> tags) {
        this.description = description;
        this.test = test;
        this.tags = tags;
    }

    public static Test test(String description, CheckedSupplier<Assertion> test) {
        return test(description, test, Collections.emptyList());
    }

    public static Test test(String description, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return new Test(description, test, tags);
    }
}
