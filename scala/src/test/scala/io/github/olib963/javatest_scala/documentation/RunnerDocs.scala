package io.github.olib963.javatest_scala.documentation

import io.github.olib963.javatest.TestRunner
import io.github.olib963.javatest_scala.JavaTestSyntax

object RunnerDocs extends JavaTestSyntax {

  // tag::definition[]
  val runner: TestRunner = Seq(
    suite("My Suite",
      test("test1")(that(true, "passes")),
      test("test2")(that(true, "passes"))
    )
  )
  // end::definition[]

  def runThem(): Unit = {
    // tag::running[]
    run(
      Seq(
        suite("My Suite",
          test("test1")(that(true, "passes")),
          test("test2")(that(true, "passes"))
        )
      )
    )
    // end::running[]
  }

}
