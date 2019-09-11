package io.github.olib963.javatest_scala.documentation

import io.github.olib963.javatest.Testable
import io.github.olib963.javatest_scala.{JavaTestSyntax, Suite}

object SuiteDocs extends Suite with JavaTestSyntax {

  // tag::varargs[]
  val suiteFromVarargs = suite("My Vararg Suite",
    test("simple test")(that(true, "passing")),
    test("addition test")(that(1 + 1 == 2, "One add one is two"))
  )
  // end::varargs[]

  // tag::seq[]
  val multiplesOf9 = Map(
    1 -> 9,
    2 -> 18,
    7 -> 63,
    456 -> 4104)

  val suiteFromSeq = suite("Multiples of 9 suite",
    multiplesOf9.map {
      case (multiple, expected) =>
        test(s"The ${multiple}th multiple of 9") {
          val result = multiple * 9
          that(result == expected, s"Should be $expected (was $result)")
        }
    }.toSeq
  )
  // end::seq[]

  // tag::trait[]
  object MySuite extends Suite with JavaTestSyntax {
    override def tests: Seq[Testable] = Seq(
      suite("Nested Suite",
        test("simple test")(that(true, "passing"))
      ),
      test("addition test")(that(1 + 1 == 2, "One add one is two"))
    )
  }
  // end::trait[]

  override def tests: Seq[Testable] = Seq(suiteFromVarargs, suiteFromSeq, MySuite)
}
