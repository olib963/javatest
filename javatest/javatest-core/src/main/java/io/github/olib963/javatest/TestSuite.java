package io.github.olib963.javatest;

import java.util.Optional;
import java.util.stream.Stream;

public interface TestSuite extends Testable {

    @Override
    default Optional<String> suiteName() {
        return Optional.of(name());
    }

    default String name() {
        return getClass().getSimpleName();
    }

    Stream<Test> tests();
}
