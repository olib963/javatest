package org.javatest;

import org.javatest.assertions.BooleanAssertion;
import org.javatest.assertions.CompositeAssertion;
import org.javatest.assertions.PendingAssertion;

/**
 * Assertions are responsible for validating that expected conditions hold true after tests have run.  
 * <p>
 * For example, a {@link BooleanAssertion} will take the boolean output from a test that has executed and ensure it is true.  
 * More complex Assertions can also be defined, e.g. to compare objects returned from tests against expectations.  
 *
 */
public interface Assertion {
    
	/**
	 * Runs the check to validate whether this Assertion holds.  
	 * @return
	 */
	AssertionResult run();

    default Assertion and(Assertion other){
        return CompositeAssertion.and(this, other);
    }

    default Assertion or(Assertion other){
        return CompositeAssertion.or(this, other);
    }

    default Assertion xor(Assertion other){
        return CompositeAssertion.xor(this, other);
    }

    static Assertion that(boolean asserted, String description) { return new BooleanAssertion(asserted, description); }

    static Assertion pending() {
        return pending("Test has not yet been written");
    }

    static Assertion pending(String reason) {
        return new PendingAssertion(reason);
    }
}
