package io.github.olib963.javatest_scala

import java.util

import io.github.olib963.javatest.{TestRunner, TestRunners}

import scala.collection.JavaConverters._

trait Runners extends TestRunners {

  def Runners(): Seq[TestRunner]

  override def runners(): util.Collection[TestRunner] = Runners.asJava

}
