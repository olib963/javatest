package io.github.olib963.javatest_sbt_interface

import io.github.olib963.javatest.{TestCompletionObserver, TestResult, TestResults, TestRunCompletionObserver}

import scala.language.implicitConversions
import java.util.function._

private [javatest_sbt_interface] object FunctionConverters {
  // Scala 11 does not handle functional interfaces well so we need to implement them directly

  implicit def scalaToJavaBinaryOperator[A](f: (A, A) => A): BinaryOperator[A] = new BinaryOperator[A] {
    override def apply(a1: A, a2: A): A = f(a1, a2)
  }

  implicit def scalaFunctionToTestCompletionObserver(f: TestResult => Unit): TestCompletionObserver = new TestCompletionObserver {
    override def onTestCompletion(result: TestResult): Unit = f(result)
  }

  implicit def scalaFunctionToTestRunCompletionObserver(f: TestResults => Unit): TestRunCompletionObserver = new TestRunCompletionObserver {
    override def onRunCompletion(results: TestResults): Unit = f(results)
  }

  implicit def scalaFunctionToJavaFunction[A, B](f: A => B): Function[A, B] = new Function[A, B] {
    override def apply(a: A): B = f(a)
  }

  implicit def scalaFunctionToJavaPredicate[A](f: A => Boolean): Predicate[A] = new Predicate[A] {
    override def test(a: A): Boolean = f(a)
  }

}
