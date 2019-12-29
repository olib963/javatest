package my.app

import io.github.olib963.javatest_scala.Runners
import io.github.olib963.javatest_scala.Suite
import io.github.olib963.javatest_scala.AllJavaTestSyntax
import scala.util.Success

object ObjectRunners extends Runners with AllJavaTestSyntax {

  val mySimpleFixture = fixture(Success("Hello"))

  override def Runners() = {
    val benchmarkedUnitTests = benchmark(runnerFromTestable(new UnitTestSuite))

    val integrationTest = fixtureRunner("sting fixture", mySimpleFixture)(word =>
      test("Simple fixture")(that(word, isEqualTo("Hello")))
    )

    Seq(benchmarkedUnitTests, integrationTest)
  }

  class UnitTestSuite extends Suite {
    override def tests = Seq(
      suite("Nested",
        test("Simple")(that(true, "passing"))
      )
    )
  }
}
