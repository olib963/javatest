package io.github.olib963.javatest_scala

import java.util

import io.github.olib963.javatest.TestRunner
import io.github.olib963.javatest.javafire.TestRunners
import CollectionConverters._

trait Runners extends TestRunners {

  def Runners: Seq[TestRunner]

  override def runners(): util.Collection[TestRunner] = toJava(Runners)

}
