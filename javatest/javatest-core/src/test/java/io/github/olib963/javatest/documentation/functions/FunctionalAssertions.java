package io.github.olib963.javatest.documentation.functions;

// This class is not tested, just compiled
// tag::include[]
import io.github.olib963.javatest.Assertion;
import io.github.olib963.javatest.AssertionResult;

public class FunctionalAssertions {

    // Create an assertion that always fails
    public static final Assertion ALWAYS_FAILING =
            () -> AssertionResult.failure("Whoops");

    // Do not attempt to run an assertion if the variable is not set
    public Assertion ensureEnvironmentVariableSet(String variable, Assertion assertion) {
        return () -> {
            if (System.getenv(variable) == null) {
                return AssertionResult.failure("You must set the environment variable " + variable);
            } else {
                return assertion.run();
            }
        };
    }
}
// end::include[]
