package io.github.olib963.javatest.eventually;

import io.github.olib963.javatest.Testable;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

import static io.github.olib963.javatest.JavaTest.test;
import static io.github.olib963.javatest.JavaTest.that;
import static io.github.olib963.javatest.eventually.Eventually.eventually;

public class InitialDelayTests {

    public static Testable.Test delayTest(ExecutorService executorService) {
        return test("Initial delay", () -> {
            var failer = new FailIfNotSet();
            executorService.submit(() -> incrementTo3(failer));
            return eventually(() ->
                            that(failer.getValue() == 3, String.format("Expected %s to have value 3 set", failer)),
                            EventualConfig.of(4, Duration.ofSeconds(1), Duration.ofSeconds(2)));
            });
    }

    private static void incrementTo3(FailIfNotSet failer) {
        for(int i = 1; i <=3; i++){
            try {
                Thread.sleep(1000L);
                failer.setValue(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class FailIfNotSet {
        private Integer value;
        public int getValue() {
            if(value == null){
                throw new IllegalStateException("Unacceptable!! The value was not set and get was called too early");
            }
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "FailIfNotSet{value=" + value + '}';
        }
    }
}
