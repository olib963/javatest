package org.javatest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class Test implements Testable {
    public final String name;
    public final CheckedSupplier<Assertion> test;
    // TODO immutable collection
    public final Collection<String> tags;
    public Test(String name, CheckedSupplier<Assertion> test, Collection<String> tags) {
        this.name = name;
        this.test = test;
        this.tags = tags;
    }

    @Override
    public Optional<String> suiteName() {
        return Optional.empty();
    }

    @Override
    public Stream<Test> tests() {
        return Stream.of(this);
    }
}
