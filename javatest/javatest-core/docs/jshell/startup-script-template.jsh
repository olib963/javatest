/env -class-path ${ABSOLUTE_PATH_TO_JAVATEST_JAR}

import io.github.olib963.javatest.*;
import static io.github.olib963.javatest.JavaTest.*;

TestResults runTest(CheckedSupplier<Assertion> testFn) {
    return runTests(test("JShell test", testFn));
}
