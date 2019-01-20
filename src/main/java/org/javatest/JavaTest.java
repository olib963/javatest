package org.javatest;

import java.util.stream.Stream;

public class JavaTest {
    public static TestResults run(Stream<Test> tests) {
        var result = tests.reduce(new TestResults(true, ""),
                (results, test) -> {
                   var success = test.test.get().holds();
                   return new TestResults(results.succeeded && success, results.testLog + System.lineSeparator() + test.description);
                },
        (a, b) -> new TestResults(a.succeeded && b.succeeded, a.testLog + System.lineSeparator() + b.testLog));
        System.out.println(result.testLog);
        return result;
    }
}
