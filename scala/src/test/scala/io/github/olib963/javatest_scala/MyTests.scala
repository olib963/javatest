package io.github.olib963.javatest_scala

import java.util.concurrent.Executors

import scala.util.Try

object MyTests extends JavaTestSyntax with FixtureSyntax {

  private val executorServiceFixture = destructibleFixture(Try(Executors.newFixedThreadPool(2)))(e => Try(e.shutdown()))

  def main(args: Array[String]): Unit = {
    val results = run(
      fixtureRunner("executor")(executorServiceFixture)(
        executor => Seq(SimpleTests, MatcherTests, EventualTests(executor))))

    if (!results.succeeded) {
      sys.error("Tests failed")
    }
  }

}
