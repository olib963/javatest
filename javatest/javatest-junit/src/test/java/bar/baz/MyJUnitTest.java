package bar.baz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyJUnitTest {

    @Test
    public void testFoo() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testBar() {
        Assertions.assertFalse(false);
    }
}
