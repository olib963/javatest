package io.github.olib963.javatest_scala

import io.github.olib963.javatest._
import io.github.olib963.javatest.matchers.Matcher

import CollectionConverters._
import FunctionConverters._
import scala.language.implicitConversions

trait JavaTestSyntax {

  def run(firstRunner: TestRunner, runners: TestRunner*): TestResults = run(firstRunner :: runners.toList)
  def run(runners: Seq[TestRunner]): TestResults = JavaTest.run(toJava(runners))

  def suite(name: String, firstTest: Testable, tests: Testable*): Testable.TestSuite = suite(name, firstTest +: tests.toSeq)
  def suite(name: String, tests: Seq[Testable]): Testable.TestSuite = JavaTest.suite(name, toJava(tests))

  def test(name: String)(test: => Assertion): Testable.Test = JavaTest.test(name, () => test)
  def pending(): Assertion = JavaTest.pending()
  def pending(message: String): Assertion = JavaTest.pending(message)

  def that(condition: Boolean, description: String): Assertion = JavaTest.that(condition, description)
  // This function apparently cannot be in a separate trait because it causes scalac to get confused about the overloading from different traits
  def that[A](value: A, matcher: Matcher[A]): Assertion = Matcher.that(value, matcher)
  def that[A](messagePrefix: String, value: A, matcher: Matcher[A]): Assertion = Matcher.that(messagePrefix, value, matcher)

  implicit def runnerFromTestable(testable: Testable): TestRunner = JavaTest.testableRunner(testable)
  implicit def runnerFromTestables(testables: Seq[Testable]): TestRunner = JavaTest.testableRunner(toJava((testables)))
  def testableRunner(testables: Seq[Testable], observers: (TestResult => Unit)*): TestRunner = {
    val asJavaObservers = observers.map(f => ((result: TestResult) => f(result)): TestCompletionObserver)
    JavaTest.testableRunner(toJava(testables), toJava(asJavaObservers))
  }

}
