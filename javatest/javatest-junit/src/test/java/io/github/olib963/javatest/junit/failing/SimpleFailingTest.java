package io.github.olib963.javatest.junit.failing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleFailingTest {

    @Test
    public void testFalseIsTrue() {
        Assertions.fail();
    }
}
