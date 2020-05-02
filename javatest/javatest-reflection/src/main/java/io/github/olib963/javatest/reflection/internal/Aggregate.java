package io.github.olib963.javatest.reflection.internal;

import io.github.olib963.javatest.javafire.TestRunners;
import io.github.olib963.javatest.TestSuiteClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Aggregate {
    public final Collection<TestSuiteClass> suites;
    public final Collection<TestRunners> runners;
    public final Collection<String> classFailures;

    private Aggregate(Collection<TestSuiteClass> suites, Collection<TestRunners> runners, Collection<String> classFailures) {
        this.suites = suites;
        this.runners = runners;
        this.classFailures = classFailures;
    }

    public static Aggregate empty() {
        return new Aggregate(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public Aggregate withSuite(TestSuiteClass suiteClass) {
        var newClasses = new ArrayList<>(suites);
        newClasses.add(suiteClass);
        return new Aggregate(newClasses, runners, classFailures);
    }

    public Aggregate withRunners(TestRunners newRunner) {
        var newRunners = new ArrayList<>(runners);
        newRunners.add(newRunner);
        return new Aggregate(suites, newRunners, classFailures);
    }

    public Aggregate withClassFailure(String classFailure) {
        var newFailedClasses = new ArrayList<>(classFailures);
        newFailedClasses.add(classFailure);
        return new Aggregate(suites, runners, newFailedClasses);
    }

    public Aggregate compose(Aggregate other) {
        var newClasses = new ArrayList<>(suites);
        newClasses.addAll(other.suites);
        var newRunners = new ArrayList<>(runners);
        newRunners.addAll(other.runners);
        var newFailedClasses = new ArrayList<>(classFailures);
        newFailedClasses.addAll(other.classFailures);
        return new Aggregate(newClasses, newRunners, newFailedClasses);
    }
}