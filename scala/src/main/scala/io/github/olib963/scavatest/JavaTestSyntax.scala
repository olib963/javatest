package io.github.olib963.scavatest

import io.github.olib963.javatest._

import scala.collection.JavaConverters._
import scala.language.implicitConversions

trait JavaTestSyntax {

  def run(runners: TestRunner*): TestResults = JavaTest.run(runners.asJava.stream())

  def suite(name: String)(tests: Testable*): Testable.TestSuite = suiteSeq(name)(tests.toSeq)
  def suiteSeq(name: String)(tests: Seq[Testable]): Testable.TestSuite = JavaTest.suite(name, tests.asJava.stream())

  def test(name: String)(test: => Assertion ): Testable.Test = JavaTest.test(name, () => test)
  def pending(name: String): Testable.Test = test(name)(JavaTest.pending())
  def pending(name: String, message: String): Testable.Test = test(name)(JavaTest.pending(message))

  def that(condition: Boolean, description: String): Assertion = JavaTest.that(condition, description)

  implicit def runnerFromTestable(testable: Testable): TestRunner = JavaTest.testableRunner(testable)
  implicit def runnerFromTestables(testables: Seq[Testable]): TestRunner = JavaTest.testableRunner(testables.asJava.stream())

}
