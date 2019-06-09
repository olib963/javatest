package io.github.olib963.javatest;

import java.util.Optional;
import java.util.stream.Stream;

public interface Testable {

    Optional<String> suiteName();

    Stream<Test> tests();
    
}
