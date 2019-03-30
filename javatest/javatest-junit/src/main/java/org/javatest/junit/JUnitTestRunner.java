package org.javatest.junit;

import org.javatest.TestResults;
import org.javatest.TestRunner;

public class JUnitTestRunner implements TestRunner {
    @Override
    public TestResults run() {
        return TestResults.init();
    }
}
