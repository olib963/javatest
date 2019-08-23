package foo;

public class Calculator {
    public static int add(int a, int b) {
        // We are intentionally making this function return the wrong value.
        // This is so you can see the tests fail, then fix the function and see them pass
        return a + b + 10;
    }
}
