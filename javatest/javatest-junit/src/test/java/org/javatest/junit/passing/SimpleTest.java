package org.javatest.junit.passing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTest {

    @Test
    public void testTrueIsTrue() {
        Assertions.assertTrue(true);
    }
}
