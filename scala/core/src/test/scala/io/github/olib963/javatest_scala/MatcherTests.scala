package io.github.olib963.javatest_scala

// tag::include[]

import io.github.olib963.javatest.Testable

import scala.util.{Failure, Success}

object MatcherTests extends Suite with JavaTestSyntax with MatcherSyntax {
  override def tests: Seq[Testable] = Seq(
    test("Equal")(that(List(1, 2, 3), isEqualTo(List(1, 2, 3)))),
    test("Type")(that("foo", hasType[String])),
    test("Instance") {
      val javaObject = new Object()
      that(javaObject, isTheSameInstanceAs(javaObject))
    },
    test("String starts with")(that("hello world", startsWith("hello"))),
    test("String ends with")(that("hello world", endsWith("world"))),
    test("String contains string")(that("hello world", containsString("o w"))),
    test("String empty")(that("", isEmptyString)),
    test("String whitespace")(that("\t\n", isBlankString)),
    test("String equal ignore case")(that("HeLlO WoRlD", isEqualToIgnoringCase("hElLo wOrLd"))),
    test("String length")(that("hello world", hasLength(11))),
    test("With custom message")(that("Because my string is empty", "", isBlankString))
  )
}

// end::include[]
// tag::scala[]
object ScalaSpecificMatcherTests extends Suite with JavaTestSyntax with MatcherSyntax {
  override def tests: Seq[Testable] = Seq(
    suite("Option tests",
      suite("defined tests",
        // TODO it should not need the type hint. Probably some variance changes in the java API would fix it.
        test("isDefined")(that(Some(1), isDefined[Int])),
        test("isDefined (empty)")(that(None, not(isDefined)))
      ),
      suite("empty tests",
        test("isEmpty")(that(None, isEmptyOption)),
        test("isEmpty (not empty)")(that(Some(1), not(isEmptyOption[Int]))),
      ),
      suite("contains tests",
        test("Contains")(that(Some(2), optionContains(2))),
        test("Contains (missing element)")(that(None, not(optionContains(4)))),
      )
    ),
    suite("Collection tests",
      suite("empty tests",
        test("isEmpty")(that(Seq(), isEmpty)),
        test("isEmpty (not empty)")(that(Seq(1, 2, 3), not(isEmpty[Int]))),
      ),
      suite("contains tests",
        test("contains")(that(Seq(1, 2, 3), contains(2))),
        test("contains (missing element)")(that(Seq(1, 2, 3), not(contains(4)))),
      ),
      suite("hasSize tests",
        test("has size")(that(Seq(1, 2, 3), hasSize[Int](3))),
        test("has size (wrong size)")(that(Seq(1, 2, 3), not(hasSize[Int](4)))),
      )
    ),
    suite("Try tests",
      suite("success tests",
        test("isSuccess")(that(Success(10), isSuccess[Int])),
        test("isSuccess (with failure)")(that(Failure(new Exception()), not(isSuccess)))
      ),
      suite("failure tests",
        test("isSuccess")(that(Failure(new Exception()), isFailure)),
        test("isSuccess (with success)")(that(Success(10), not(isFailure[Int])))
      )
    )
  )
}
// end::scala[]
