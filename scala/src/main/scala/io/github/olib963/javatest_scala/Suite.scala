package io.github.olib963.javatest_scala

import io.github.olib963.javatest.{TestSuiteClass, Testable}
import scala.collection.JavaConverters._

trait Suite extends TestSuiteClass {

  def tests: Seq[Testable]

  override def testables() = tests.asJava

}
