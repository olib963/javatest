package org.javatest;

import java.util.Collection;

public final class Test {
    public final String name;
    public final CheckedSupplier<Assertion> test;
    // TODO immutable collection
    public final Collection<String> tags;
    public Test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        this.name = name;
        this.test = test;
        this.tags = tags;
    }

    // TODO it may be useful to have a couple of helper functions such as
    // mapName(f: String => String)
    // withTag(f: String => String)
    // after(f: Assertion => Assertion)
    // around(f: (() => Assertion) => (() => Assertion)
    // Or some such functions
}
