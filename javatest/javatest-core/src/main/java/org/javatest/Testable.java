package org.javatest;

import java.util.Optional;
import java.util.stream.Stream;

// TODO there should be some more intuitive name for this interface
public interface Testable {

    Optional<String> suiteName();

    Stream<Test> tests();
    
}
