package org.javatest;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class Test {
    public final String description;
    public final Supplier<Assertion> test;
    public final Collection<String> tags;
    public Test(String description, Supplier<Assertion> test) {
        this(description, test, Collections.emptyList());
    }
    public Test(String description, Supplier<Assertion> test, Collection<String> tags) {
        this.description = description;
        this.test = test;
        this.tags = tags;
    }
}
