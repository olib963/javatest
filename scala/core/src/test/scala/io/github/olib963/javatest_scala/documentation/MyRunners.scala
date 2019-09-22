package io.github.olib963.javatest_scala.documentation

import io.github.olib963.javatest.TestRunner
import io.github.olib963.javatest_scala._

// tag::include[]
object MyRunners extends Runners {
  override def Runners: Seq[TestRunner] = {

    val simpleRunner: TestRunner =
      Seq(
        suite("Suite1", test("test")(pending())),
        suite("Suite2", test("test")(pending()))
      )

    Seq(
      simpleRunner
      // Other runner definitions ...
    )
  }
}
// end::include[]
