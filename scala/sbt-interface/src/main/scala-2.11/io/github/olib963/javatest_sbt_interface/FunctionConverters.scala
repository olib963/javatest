package io.github.olib963.javatest_sbt_interface

import io.github.olib963.javatest.{TestRunCompletionObserver, TestResults}

import scala.language.implicitConversions
import java.util.function._

private [javatest_sbt_interface] object FunctionConverters {
  // Scala 11 does not handle functional interfaces well so we need to implement them directly

  implicit def scalaToJavaBinaryOperator[A](f: (A, A) => A): BinaryOperator[A] = new BinaryOperator[A] {
    override def apply(a1: A, a2: A): A = f(a1, a2)
  }

  implicit def scalaFunctionToTestRunCompletionObserver(f: TestResults => Unit): TestRunCompletionObserver = new TestRunCompletionObserver {
    override def onRunCompletion(results: TestResults): Unit = f(results)
  }

}
