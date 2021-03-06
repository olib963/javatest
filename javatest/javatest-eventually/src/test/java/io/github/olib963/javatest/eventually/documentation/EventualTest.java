package io.github.olib963.javatest.eventually.documentation;

import io.github.olib963.javatest.Testable.Test;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
// tag::content[]
import static io.github.olib963.javatest.JavaTest.*;
import static io.github.olib963.javatest.eventually.Eventually.eventually;

public class EventualTest {

    public static Test eventualTest(ExecutorService executor) {
        return test("Map is populated", () -> {
            var map = new HashMap<String, String>();
            // You can imagine this is some asynchronous task in your code base.
            executor.submit(() -> {
                // Due to this 1 second sleep the assertion will not hold straight away.
                try { Thread.sleep(1000L); } catch (InterruptedException e) {}
                map.put("foo", "bar");
            });
            return eventually(() ->
                    that("bar".equals(map.get("foo")), "Map contains entry foo:bar"));
        });
    }
}
// end::content[]
