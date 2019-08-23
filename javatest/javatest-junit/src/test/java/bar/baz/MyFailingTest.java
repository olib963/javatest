package bar.baz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyFailingTest {

    @Test
    public void failTest() {
        Assertions.assertTrue(false);
    }
}
