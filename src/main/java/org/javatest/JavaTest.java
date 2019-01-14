package org.javatest;

public class JavaTest {

    public static boolean run() {
        return true;
    }

    public static void main(String... args) {
        System.out.println("YAY");
        assert JavaTest.run();
    }
}
