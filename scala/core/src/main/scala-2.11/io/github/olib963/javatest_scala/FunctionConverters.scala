package io.github.olib963.javatest_scala

import io.github.olib963.javatest.{CheckedSupplier, TestCompletionObserver, TestResult}

import scala.concurrent.duration.Duration
import scala.language.implicitConversions
import java.util.function._

private [javatest_scala] object FunctionConverters {
  // Scala 11 does not handle functional interfaces well so we need to implement them directly

  implicit def scalaToJavaPredicate[A](p: A => Boolean): Predicate[A] = new Predicate[A] {
    override def test(a: A): Boolean = p(a)
  }

  implicit def scalaToJavaFunction[A, B](f: A => B): Function[A, B] = new Function[A, B] {
    override def apply(a: A): B = f(a)
  }

  implicit def scalaToJavaSupplier[A](f: () => A): Supplier[A] = new Supplier[A] {
    override def get(): A = f()
  }

  implicit def scalaToJavaRunnable[A](f: () => A): java.lang.Runnable = new Runnable {
    override def run(): Unit = f()
  }

  implicit def scalaFunctionToTestCompletionObserver(f: TestResult => Unit): TestCompletionObserver = new TestCompletionObserver {
    override def onTestCompletion(result: TestResult): Unit = f(result)
  }

  implicit def scalaFunctionToCheckedSupplier[A](s: () => A): CheckedSupplier[A] = new CheckedSupplier[A] {
    override def get(): A = s()
  }

  implicit def scalaFunctionToDurationFormat(f: Duration => String): DurationFormat = new DurationFormat {
    override def apply(d: Duration): String = f(d)
  }

}
