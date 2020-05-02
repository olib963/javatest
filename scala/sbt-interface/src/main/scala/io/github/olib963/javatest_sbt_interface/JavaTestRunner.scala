package io.github.olib963.javatest_sbt_interface

import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors

import io.github.olib963.javatest.TestResults
import io.github.olib963.javatest_scala.{Runners, Suite}
import sbt.testing.{Runner, Task, TaskDef}

import scala.util.Try

class JavaTestRunner(val args: Array[String], val remoteArgs: Array[String], val testClassLoader: ClassLoader) extends Runner {
  val allResults = new AtomicReference(TestResults.empty())

  override def done(): String = {
    val results = allResults.get()
    val logs = CollectionConverters.toScala(results.allLogs().collect(Collectors.toList[String]))
    val totals =
      s"""Ran a total of ${results.testCount()} tests.
         |${results.successCount} succeeded
         |${results.failureCount} failed
         |${results.pendingCount} pending
         |""".stripMargin
    if (logs.isEmpty) totals else totals + "\n" + logs.mkString("\n")
  }

  override def tasks(taskDefs: Array[TaskDef]): Array[Task] =
    taskDefs.collect {
      case taskDef if taskDef.fingerprint() == JavaTestScalaFramework.SuiteFingerprint =>
        new JavaTestTask(moduleOf[Suite](taskDef).map(Seq(_)), taskDef, allResults)
      case taskDef if taskDef.fingerprint() == JavaTestScalaFramework.RunnerFingerprint =>
        new JavaTestTask(moduleOf[Runners](taskDef).map(_.Runners), taskDef, allResults)
    }

  private def moduleOf[A](task: TaskDef): Try[A] = Try {
    testClassLoader.loadClass(s"${task.fullyQualifiedName()}$$")
      .getDeclaredField("MODULE$")
      .get(null)
      .asInstanceOf[A]
  }
}
