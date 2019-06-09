package io.github.olib963.javatest.logging;

import io.github.olib963.javatest.AssertionResult;

public enum Colour {

    RED("\u001B[31m"), YELLOW("\u001B[33m"), GREEN("\u001B[32m");

    private final String colourCode;

    Colour(final String colourCode) {
        this.colourCode = colourCode;
    }

    public String getCode() {
        return colourCode;
    }

    public static final String RESET_CODE = "\u001B[0m";

    public static Colour forResult(AssertionResult result) {
        if (result.pending) {
            return Colour.YELLOW;
        } else if (result.holds) {
            return Colour.GREEN;
        } else {
            return Colour.RED;
        }
    }

}
