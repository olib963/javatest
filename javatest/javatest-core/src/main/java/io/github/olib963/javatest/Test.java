package io.github.olib963.javatest;

import java.util.Optional;
import java.util.stream.Stream;

public final class Test implements Testable {
    public final String name;
    public final CheckedSupplier<Assertion> test;
    Test(String name, CheckedSupplier<Assertion> test) {
        this.name = name;
        this.test = test;
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
