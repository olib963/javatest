package org.javatest.logging;

public enum Colour {

    RED("\u001B[31m"), YELLOW("\u001B[33m"), GREEN("\u001B[32m"), INVISIBLE("\033[37m"), WHITE(resetCode());

    private final String colourCode;

    Colour(final String colourCode) {
        this.colourCode = colourCode;
    }

    public String getCode() {
        return colourCode;
    }

    public static String resetCode() {
        return "\u001B[0m";
    }

}
