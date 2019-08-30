package io.github.olib963.javatest.testable;

import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.CheckedSupplier;
import io.github.olib963.javatest.Testable;

public final class Test implements Testable {
    public final String name;
    public final CheckedSupplier<Assertion> test;
    public Test(String name, CheckedSupplier<Assertion> test) {
        this.name = name;
        this.test = test;
    }
}
