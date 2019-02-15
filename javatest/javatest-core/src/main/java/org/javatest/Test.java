package org.javatest;

import java.util.Collection;

public interface Test {

    String name();

    CheckedSupplier<Assertion> test();

    Collection<String> tags();
}
