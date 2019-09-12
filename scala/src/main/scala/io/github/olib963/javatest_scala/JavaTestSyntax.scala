package io.github.olib963.javatest_scala

import io.github.olib963.javatest._
import io.github.olib963.javatest.matchers.Matcher

import scala.collection.JavaConverters._
import scala.language.implicitConversions

trait JavaTestSyntax {

  def run(runners: TestRunner*): TestResults = JavaTest.run(runners.asJava)

  def suite(name: String, firstTest: Testable, tests: Testable*): Testable.TestSuite = suite(name, firstTest +: tests.toSeq)
  def suite(name: String, tests: Seq[Testable]): Testable.TestSuite = JavaTest.suite(name, tests.asJava)

  def test(name: String)(test: => Assertion): Testable.Test = JavaTest.test(name, () => test)
  def pending(): Assertion = JavaTest.pending()
  def pending(message: String): Assertion = JavaTest.pending(message)

  def that(condition: Boolean, description: String): Assertion = JavaTest.that(condition, description)
  // This function apparently cannot be in a separate trait because it causes scalac to get confused about the overloading from different traits
  def that[A](value: A, matcher: Matcher[A]): Assertion = Matcher.that(value, matcher)
  def that[A](messagePrefix: String, value: A, matcher: Matcher[A]): Assertion = Matcher.that(messagePrefix, value, matcher)

  implicit def runnerFromTestable(testable: Testable): TestRunner = JavaTest.testableRunner(testable)
  implicit def runnerFromTestables(testables: Seq[Testable]): TestRunner = JavaTest.testableRunner(testables.asJava)

}
