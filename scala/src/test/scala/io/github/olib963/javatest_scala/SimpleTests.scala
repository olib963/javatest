package io.github.olib963.javatest_scala
import io.github.olib963.javatest.Testable

object SimpleTests extends Suite with JavaTestSyntax {
  override def tests: Seq[Testable] = Seq(
    suite("Simple Suite",
      suite("Pending Suite", Seq(
        test("My first pending test")(pending()),
        test("My second pending test")(pending("because I haven't implemented it yet"))
      )),
      test("Passing test")(that(true, "passing")),
      // tag::include[]
      test("Passing multiline test"){
        val word = "HELLO WORLD"
        val expected = "hello world"
        val actual = word.toLowerCase()
        that(actual == expected, s"Expected $word to lower case to be $expected (was $actual)")
      }
      // end::include[]
    )
  )
}
