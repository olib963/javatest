package io.github.olib963.javatest.documentation.functions;

// This class is not tested, just compiled
// tag::include[]
import io.github.olib963.javatest.TestResults;
import io.github.olib963.javatest.TestRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public class FunctionalRunners {
    // The simplest test runner: runs nothing and returns no results
    public static final TestRunner EMPTY_RUNNER = config -> TestResults.empty();

    private static final Set<DayOfWeek> WEEKEND =
            Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    // Wrap another test runner such that it will not run anything on the weekend
    public TestRunner onlyRunOnWeekDays(TestRunner runner) {
        var today = LocalDate.now();
        return config -> WEEKEND.contains(today.getDayOfWeek()) ?
                EMPTY_RUNNER.run(config) : runner.run(config);
    }
}
// end::include[]
