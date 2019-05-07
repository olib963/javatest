package org.javatest.eventually;

import org.javatest.Test;
import org.javatest.TestSuite;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static org.javatest.JavaTest.*;
import static org.javatest.eventually.Eventually.*;

public class InitialDelayTests implements TestSuite {
    private final ExecutorService executorService;

    public InitialDelayTests(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Stream<Test> testStream() {
        return Stream.of(
                test("Initial delay", () -> {
                    var failer = new FailIfNotSet();
                    executorService.submit(() -> incrementTo3(failer));
                    return eventually(() ->
                            that(failer.getValue() == 3, String.format("Expected %s to have value 3 set", failer)),
                            EventualConfig.of(4, Duration.ofSeconds(1), Duration.ofSeconds(2))
                    );
                })
        );
    }

    private void incrementTo3(FailIfNotSet failer) {
        for(int i = 1; i <=3; i++){
            try {
                Thread.sleep(1000L);
                failer.setValue(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class FailIfNotSet {
        private Integer value;
        private boolean calledEarly = false;

        public int getValue() {
            if (value == null) {
                calledEarly = true;
            }
            if(calledEarly){
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
