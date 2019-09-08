package io.github.olib963.javatest_scala.documentation

import io.github.olib963.javatest.Testable
import io.github.olib963.javatest_scala.{BenchmarkSyntax, DurationFormat, JavaTestSyntax, Suite}

import scala.concurrent.duration._

object BenchmarkDocumentation extends Suite with JavaTestSyntax with BenchmarkSyntax {

  // tag::include[]
  val passingAssertion = that(true, "always passes")

  val customFormattedTest = {
    implicit val customFormatter: DurationFormat = d => s"${d.toMillis}ms"
    benchmark(test("Benchmarked Test")(passingAssertion))
  }

  val timedTest = failIfLongerThan(2.seconds)(
    test("Test with time limit") {
      Thread.sleep(1.second.toMillis)
      passingAssertion
    }
  )
  // end::include[]
  override def tests: Seq[Testable] = Seq(customFormattedTest, timedTest)
}
