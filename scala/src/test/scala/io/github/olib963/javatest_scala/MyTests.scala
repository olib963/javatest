package io.github.olib963.javatest_scala

import java.util.concurrent.Executors

import scala.util.Try

object MyTests extends JavaTestSyntax with FixtureSyntax with BenchmarkSyntax {

  private val executorServiceFixture = destructibleFixture(Try(Executors.newFixedThreadPool(2)))(e => Try(e.shutdown()))

  def main(args: Array[String]): Unit = {
    val results = run(benchmark(
      fixtureRunner("executor")(executorServiceFixture)(
        executor => Seq(SimpleTests, MatcherTests, EventualTests(executor)))))

    if (!results.succeeded) {
      sys.error("Tests failed")
    }
  }

}
