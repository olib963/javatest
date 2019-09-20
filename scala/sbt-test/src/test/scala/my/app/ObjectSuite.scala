package my.app

import io.github.olib963.javatest.Testable

import io.github.olib963.javatest_scala._

object ObjectSuite extends Suite {
  override def tests: Seq[Testable] = Seq(
    test("Simple")(that(true, "passing"))
  )
}
