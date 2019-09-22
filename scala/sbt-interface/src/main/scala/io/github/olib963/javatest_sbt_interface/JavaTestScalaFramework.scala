package io.github.olib963.javatest_sbt_interface

import io.github.olib963.javatest.{JavaTest, TestResults, TestRunner}
import io.github.olib963.javatest_scala.{Runners, Suite}
import sbt.testing.{Event, EventHandler, Fingerprint, Framework, Logger, OptionalThrowable, Runner, Selector, Status, SubclassFingerprint, Task, TaskDef}

import scala.reflect.ClassTag
import scala.util.Try

class JavaTestScalaFramework extends Framework {
  override def name(): String = "JavaTest-Scala"

  override def fingerprints(): Array[Fingerprint] = Array(
    JavaTestScalaFramework.SuiteFingerprint,
    JavaTestScalaFramework.RunnerFingerprint
  )

  override def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    JTRunner(args, remoteArgs, testClassLoader)
}

object JavaTestScalaFramework {

  class ScalaFingerPrint[A](implicit classTag: ClassTag[A]) extends SubclassFingerprint {
    override def isModule: Boolean = true
    override def requireNoArgConstructor(): Boolean = true
    override def superclassName(): String = classTag.runtimeClass.getName
    override def equals(obj: Any): Boolean = obj match {
      case classBased: SubclassFingerprint =>
        classBased.isModule && classBased.requireNoArgConstructor() && classBased.superclassName() == this.superclassName()
      case _ => false
    }
    override def toString: String = s"Fingerprint for ${superclassName()}"
  }
  val SuiteFingerprint = new ScalaFingerPrint[Suite]
  val RunnerFingerprint = new ScalaFingerPrint[Runners]
}

// TODO I think the assumption is that you return separate tasks per definition passed rather than defining a run up front.
// TODO for now the simplest thing I could think of was to just return constants and make the runner invoke JavaTest. Need to revisit to implement integration with events & logging etc.
case class JTRunner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader) extends Runner {
  override def done(): String = ""

  // TODO error handling etc.
  override def tasks(taskDefs: Array[TaskDef]): Array[Task] = {
    val testsCases = taskDefs.flatMap { t => t.fingerprint() match {
      case JavaTestScalaFramework.SuiteFingerprint => Some(Left(moduleOf[Suite](t.fullyQualifiedName())))
      case JavaTestScalaFramework.RunnerFingerprint => Some(Right(moduleOf[Runners](t.fullyQualifiedName())))
      case other => None
    }}

    val singleTestRunner: TestRunner = testsCases.collect{ case Left(suite) => suite}.toSeq
    val runners = singleTestRunner +: testsCases.collect{ case Right(runner) => runner}.flatMap(_.Runners).toSeq
    import io.github.olib963.javatest_scala.CollectionConverters.toJava
    val results = JavaTest.run(toJava(runners))
    Array(JTTask(results))
  }

  private def moduleOf[A](classToLoad: String): A =
    testClassLoader.loadClass(s"$classToLoad$$").getDeclaredField("MODULE$").get(null).asInstanceOf[A]
}

// This task does not actually seem to be invoked by SBT. TODO Fix the fingerprints and try to return the results for each test?
case class JTTask(results: TestResults) extends Task {
  val fakeFingerprint = new Fingerprint {
  }
  override def tags(): Array[String] = Array()

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    if(!results.succeeded){
      eventHandler.handle(new Event{
        override def fullyQualifiedName(): String = "Failure"
        override def fingerprint(): Fingerprint = fakeFingerprint
        override def selector(): Selector = null
        override def status(): Status = Status.Failure
        override def throwable(): OptionalThrowable = new OptionalThrowable()
        override def duration(): Long = 0
      })
    }
    Array()
  }

  override def taskDef(): TaskDef = new TaskDef("", fakeFingerprint, false, Array())
}
