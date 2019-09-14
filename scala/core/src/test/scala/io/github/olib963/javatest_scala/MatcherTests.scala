package io.github.olib963.javatest_scala
// tag::include[]
import io.github.olib963.javatest.Testable

object MatcherTests extends Suite with JavaTestSyntax with MatcherSyntax {
  override def tests: Seq[Testable] = Seq(
    test("Equal")(that(List(1, 2, 3), isEqualTo(List(1, 2, 3)))),
    test("Type")(that("foo", hasType[String])),
    test("Instance"){
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
