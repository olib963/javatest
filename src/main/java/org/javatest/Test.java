package org.javatest;

public class Test {
    public final String description;
    public final Assertion assertion;
    public Test(String description, Assertion assertion) {
        this.description = description;
        this.assertion = assertion;
    }
}
