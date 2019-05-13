package org.javatest;

import java.util.Optional;
import java.util.stream.Stream;

public interface TestSuite extends Testable {

    @Override
    default Optional<String> suiteName() {
        return Optional.of(getClass().getSimpleName());
    }

    Stream<Test> tests();
}
