package io.github.olib963.javatest_scala

import io.github.olib963.javatest.{TestSuiteClass, Testable}
import CollectionConverters._

trait Suite extends TestSuiteClass {

  def tests: Seq[Testable]

  override def testables() = toJava(tests)

}
