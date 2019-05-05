package org.javatest.tests;

import org.javatest.Assertion;
import org.javatest.CheckedSupplier;
import org.javatest.Test;

import java.util.Collection;

public class SimpleTest implements Test {
    private final String name;
    private final CheckedSupplier<Assertion> test;
    private final Collection<String> tags;
    public SimpleTest(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        this.name = name;
        this.test = test;
        this.tags = tags;
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
