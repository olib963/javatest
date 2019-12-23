package my.app

import io.github.olib963.javatest.Testable

import io.github.olib963.javatest_scala._
import io.github.olib963.javatest_scala.scalacheck._
import org.scalacheck.Gen

object ScalacheckSuite extends Suite {
  // TODO should it be propertyTest("description", gens)(test) ?
  override def tests: Seq[Testable] = Seq(
    test("Sqrt")(forAll(Gen.posNum[Int]) { n =>
      val m = math.sqrt(n.toDouble)
      that(s"Square root of $n squared is $n", math.round(m * m), isEqualTo[Long](n))
    }),
    suite("List Properties",
      test("List tail")(forAll { (n: Int, l: List[Int]) =>
        that("Tail of a list with a prepended element is the original list", (n :: l).tail, isEqualTo(l))
      }),
      test("List reverse")(forAll { l: List[String] => that(l.reverse.reverse, isEqualTo(l)) }),
      test("List head")(forAll { l: List[Int] =>
        if (l.isEmpty) {
          that("Head of an empty list is empty", l.headOption, isEmptyOption[Int])
        } else {
          that("Head of non empty list is the first element", l.head, isEqualTo(l(0)))
        }
      }))
  )
}
