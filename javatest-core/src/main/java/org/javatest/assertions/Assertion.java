package org.javatest.assertions;

public interface Assertion {

    AssertionResult run();

    // TODO composite assertion descriptions
    // TODO colour and split the ones that are an issue
    default Assertion and(Assertion other){
        // TODO split out description from other logs, need the assertions to have descriptions so that we can compose them.
       return that(this.run().holds && other.run().holds, "");
    }

    default Assertion or(Assertion other){
        return that(this.run().holds || other.run().holds, "");
    }

    default Assertion xor(Assertion other){
        return that(this.run().holds ^ other.run().holds, "");
    }

    static Assertion that(boolean asserted, String description) { return new BooleanAssertion(asserted, description); }

    static Assertion pending() {
        return pending("Test has not yet been written");
    }

    static Assertion pending(String reason) {
        return new PendingAssertion(reason);
    }

}
