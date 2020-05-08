package io.github.olib963.javatest.documentation;

// tag::lazy-imports[]
import java.util.stream.Stream;
// end::lazy-imports[]

// tag::normal-imports[]
import java.util.Collections;
import java.util.List;
// end::normal-imports[]
// tag::imports[]
import io.github.olib963.javatest.*;

import static io.github.olib963.javatest.JavaTest.*;
// end::imports[]

public class RunnerDocumentation {

    // tag::normal[]
    public class MyRunners {

        public TestRunner singleTestRunner = testableRunner(List.of(
                test("Simple test", () -> pending())));

        public TestRunner suiteTests = testableRunner(
                List.of(MyFirstTestSuite.mySuite(), new ClassAsSuite())
        );
    }
    // end::normal[]
    // tag::lazy[]
    public class LazyRunners {
        private Stream<Testable.Test> oneHundredLazyTests =
                Stream.generate(() -> test("Lazy test", () -> pending()))
                        .limit(100);

        public TestRunner lazyRunner = lazyTestableRunner(oneHundredLazyTests);

    }
    // end::lazy[]


}
