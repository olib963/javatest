package io.github.olib963.javatest_scala

import java.util.concurrent.Executors

import io.github.olib963.javatest.TestRunner
import io.github.olib963.javatest_scala.documentation._

import scala.util.Try

object MyTests {

  // The following line would be needed to inherit all syntax if we weren't already in that package
  // import io.github.olib963.javatest_scala._

  private val executorServiceFixture = destructibleFixture(Try(Executors.newFixedThreadPool(2)))(e => Try(e.shutdown()))

  def main(args: Array[String]): Unit = {
    val testRunner = fixtureRunner("executor", executorServiceFixture)(
      executor => Seq(SimpleTests, MatcherTests, ScalaSpecificMatcherTests, EventualTests(executor)))

    val results = run(benchmark(testRunner))

    if (!results.succeeded) {
      sys.error("Scala tests failed")
    }

    val documentationResults = run(
      Seq[TestRunner](
        FixtureDocumentation.runnerUsingSimpleFixture,
        FixtureDocumentation.runner1,
        FixtureDocumentation.runner2,
        BenchmarkDocumentation,
        SuiteDocs
      ) ++ MyRunners.Runners
    )

    if (!documentationResults.succeeded) {
      sys.error("Documentation tests failed")
    }
  }

}
