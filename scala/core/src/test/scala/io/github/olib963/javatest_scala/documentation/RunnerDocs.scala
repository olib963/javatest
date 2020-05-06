package io.github.olib963.javatest_scala.documentation

import io.github.olib963.javatest.{RunConfiguration, TestResults, TestRunner}
import io.github.olib963.javatest_scala.JavaTestSyntax

import io.github.olib963.javatest_scala.FunctionConverters._

object RunnerDocs extends JavaTestSyntax {

  // tag::definition[]
  val runner: TestRunner = Seq(
    suite("My Suite",
      test("test1")(that(true, "passes")),
      test("test2")(that(true, "passes"))
    )
  )
  // end::definition[]

  def runDirectly() =
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

  def runWithCustomConfig() =
  // tag::customConfig[]
    run(
      Seq(test("test1")(that(true, "passes")): TestRunner),
      RunConfiguration.empty().addRunObserver((results: TestResults) => if(results.pendingCount > 0) println("Tests still need to be written"))
    )
  // end::customConfig[]

}
