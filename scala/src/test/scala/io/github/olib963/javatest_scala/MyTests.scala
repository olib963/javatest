package io.github.olib963.javatest_scala

import java.util.concurrent.Executors

import io.github.olib963.javatest_scala.documentation.{BenchmarkDocumentation, FixtureDocumentation}

import scala.util.Try

object MyTests {

  // The following line would be needed to inherit all syntax if we weren't already in that package
  //  import io.github.olib963.javatest_scala._

  private val executorServiceFixture = destructibleFixture(Try(Executors.newFixedThreadPool(2)))(e => Try(e.shutdown()))

  def main(args: Array[String]): Unit = {
    val results = run(benchmark(
      fixtureRunner("executor", executorServiceFixture)(
        executor => Seq(SimpleTests, MatcherTests, EventualTests(executor)))))

    if (!results.succeeded) {
      sys.error("Scala tests failed")
    }

    val documentationResults = run(
      FixtureDocumentation.runnerUsingSimpleFixture,
      FixtureDocumentation.runner1,
      FixtureDocumentation.runner2,
      BenchmarkDocumentation
    )

    if (!documentationResults.succeeded) {
      sys.error("Documentation tests failed")
    }
  }

}
