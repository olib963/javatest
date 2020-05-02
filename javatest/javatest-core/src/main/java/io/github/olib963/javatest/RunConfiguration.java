package io.github.olib963.javatest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class RunConfiguration {

    private final Collection<TestCompletionObserver> testObservers;
    private final Collection<TestRunCompletionObserver> runObservers;

    private RunConfiguration(Collection<TestCompletionObserver> testObservers, Collection<TestRunCompletionObserver> runObservers) {
        this.testObservers = testObservers;
        this.runObservers = runObservers;
    }

    public static RunConfiguration defaultConfig() {
        return new RunConfiguration(
                Collections.singletonList(TestCompletionObserver.colourLogger()),
                Collections.singletonList(TestRunCompletionObserver.logger())
        );
    }

    public static RunConfiguration empty() {
        return new RunConfiguration(Collections.emptyList(), Collections.emptyList());
    }

    public Stream<TestCompletionObserver> testObservers() {
        return testObservers.stream();
    }

    public Stream<TestRunCompletionObserver> runObservers() {
        return runObservers.stream();
    }

    public RunConfiguration addTestObserver(TestCompletionObserver observer) {
                return addTestObservers(Collections.singletonList(observer));
    }

    public RunConfiguration addTestObservers(Collection<TestCompletionObserver> observers) {
        var copy = new ArrayList<>(testObservers);
        copy.addAll(observers);
        return new RunConfiguration(copy, runObservers);
    }

    public RunConfiguration addRunObserver(TestRunCompletionObserver observer) {
        return addRunObservers(Collections.singletonList(observer));
    }

    public RunConfiguration addRunObservers(Collection<TestRunCompletionObserver> observers) {
        var copy = new ArrayList<>(runObservers);
        copy.addAll(observers);
        return new RunConfiguration(testObservers, copy);
    }

}
