package io.github.olib963.javatest_scala
import io.github.olib963.javatest.Testable

object SimpleTests extends Suite with JavaTestSyntax {
  override def tests: Seq[Testable] = Seq(
    suite("Simple Suite")(
      suiteSeq("Pending Suite")(Seq(
        pending("My first pending test"),
        pending("My second pending test", "because I haven't implemented it yet")
      )),
      test("Passing test")(that(true, "passing")),
      test("Passing function test"){
        val word = "HELLO WORLD"
        val expected = "hello world"
        val actual = word.toLowerCase()
        that(actual == expected, s"Expected $word to lower case to be $expected (was $actual)")
      }
    )
  )
}
