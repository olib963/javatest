package foo;

import java.util.stream.Stream;

import static io.github.olib963.javatest.JavaTest.*;

public class Tests {

    public static void main(String... args) {
        var result = runTests(Stream.of(
                test("Addition", () -> that(1 + 1 == 2, "Math still works, one add one is still two")),
                test("Calculator Addition", () -> {
                    var one = 1;
                    var expected = 2;
                    var additionResult = Calculator.add(1, 1);
                    var description = "Expected %s add %s to be %s (Calculator returned %s)";
                    var formatted = String.format(description, one, one, expected, additionResult);
                    return that(additionResult == expected, formatted);
                })));
        if (!result.succeeded) {
            throw new RuntimeException("Tests failed!");
        }
        System.out.println("Tests passed");
    }
}