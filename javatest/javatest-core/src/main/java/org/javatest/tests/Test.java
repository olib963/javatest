package org.javatest.tests;

import org.javatest.assertions.Assertion;

import java.util.Collection;
import java.util.Collections;

public class Test {
    public final String name;
    public final CheckedSupplier<Assertion> test;
    public final Collection<String> tags;
    Test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        this.name = name;
        this.test = test;
        this.tags = tags;
    }

    public static Test test(String name, CheckedSupplier<Assertion> test) {
        return test(name, test, Collections.emptyList());
    }

    public static Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return new Test(name, test, tags);
    }
}
