package org.javatest.tests;

import org.javatest.CheckedSupplier;
import org.javatest.Assertion;
import org.javatest.Test;

import java.util.Collection;
import java.util.Collections;

public class SimpleTest implements Test {
    private final String name;
    private final CheckedSupplier<Assertion> test;
    private final Collection<String> tags;
    private SimpleTest(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        this.name = name;
        this.test = test;
        this.tags = tags;
    }

    public static Test test(String name, CheckedSupplier<Assertion> test) {
        return test(name, test, Collections.emptyList());
    }

    public static Test test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        return new SimpleTest(name, test, tags);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public CheckedSupplier<Assertion> test() {
        return test;
    }

    @Override
    public Collection<String> tags() {
        return tags;
    }
}
