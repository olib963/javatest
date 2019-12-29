package my.app

import io.github.olib963.javatest.Testable.Test
import io.github.olib963.javatest.matchers.internal.PredicateMatcher
import io.github.olib963.javatest.{JavaTest, TestResults, TestRunCompletionObserver, TestRunner, Testable}
// tag::imports[]
import io.github.olib963.javatest_scala._
import io.github.olib963.javatest_scala.scalacheck._
import org.scalacheck.Gen
// end::imports[]
// TODO should it be propertyTest("description", gens)(test) ?
// tag::include[]
object ScalacheckSuite extends Suite {

  override def tests: Seq[Testable] = Seq(
    test("Pending scalacheck test")(
      forAll { s: String => pending("Not yet written") }
    ),
    test("Sqrt")(forAll(Gen.posNum[Int]) { n =>
      val m = math.sqrt(n.toDouble)
      that(s"Square root of $n squared is $n", math.round(m * m), isEqualTo[Long](n))
    }),
    suite("List Properties",
      test("List tail")(forAll { (n: Int, l: List[Int]) =>
        that("Tail of a list with a prepended element is the original list", (n :: l).tail, isEqualTo(l))
      }),
      test("List reverse")(forAll { l: List[String] =>
        that(l.reverse.reverse, isEqualTo(l))
      }),
      test("List head")(forAll { l: List[Int] =>
        if (l.isEmpty) {
          that("Head of an empty list is empty", l.headOption, isEmptyOption[Int])
        } else {
          that("Head of non empty list is the first element", l.head, isEqualTo(l(0)))
        }
      })
    ),
    // end::include[]
    suite("Failing Tests",
      test("Failing sqrt") {
        val failingTest = test("Test")(forAll(Gen.posNum[Int])(n =>
          that(s"Square root of $n squared is $n", (n: Long) * (n: Long), isEqualTo[Long](n))
        ))
        val result = runTestNoLogging(failingTest)
        that("Because the square root check is wrong", result, failed)
      },
      test("Exhausted sqrt") {
        val failingTest = test("Test")(forAll(Gen.fail[Int])(_ => that(true, "always passing")))
        val result = runTestNoLogging(failingTest)
        that("Because the generator never returns anything", result, failed)
      },
      test("Assertion error") {
        val failingTest = test("Test")(forAll(Gen.posNum[Int]) { _ => assert(false, "Whoops"); pending() })
        val result = runTestNoLogging(failingTest)
        that("Because the we threw an assertion error", result, failed)
      },
      test("Exception") {
        val failingTest = test("Test")(forAll(Gen.posNum[Int]) { _ => sys.error("Whoops") })
        val result = runTestNoLogging(failingTest)
        that("Because the we threw an exception", result, failed)
      }
    )
  )

  // Hide the logging from failed tests
  private def runTestNoLogging(test: Test) =
    JavaTest.run(java.util.List.of[TestRunner](testableRunner(Seq(test))), java.util.Collections.emptyList[TestRunCompletionObserver])

  // Scala 11 is bad with functional interfaces
  private val failed = PredicateMatcher.of(new java.util.function.Predicate[TestResults]{
    override def test(results: TestResults): Boolean = !results.succeeded
  }, "have failed")
}
