package io.github.olib963.javatest_sbt_interface

import java.util.Collections

import io.github.olib963.javatest.{JavaTest, TestRunCompletionObserver, TestRunner}
import io.github.olib963.javatest_scala.{Runners, Suite}
import sbt.testing.{Event, EventHandler, Fingerprint, Framework, Logger, OptionalThrowable, Runner, Selector, Status, SubclassFingerprint, Task, TaskDef}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

class JavaTestScalaFramework extends Framework {
  override def name(): String = "JavaTest-Scala"

  override def fingerprints(): Array[Fingerprint] = Array(
    JavaTestScalaFramework.SuiteFingerprint,
    JavaTestScalaFramework.RunnerFingerprint
  )

  override def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    JavaTestRunner(args, remoteArgs, testClassLoader)
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

case class JavaTestRunner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader) extends Runner {
  override def done(): String = "Complete"

  override def tasks(taskDefs: Array[TaskDef]): Array[Task] =
    taskDefs.collect {
      case t if t.fingerprint() == JavaTestScalaFramework.SuiteFingerprint =>
        val toRun: () => Seq[TestRunner] = () => List(moduleOf[Suite](t.fullyQualifiedName()))
        JavaTestTask(toRun, t)
      case t if t.fingerprint() == JavaTestScalaFramework.RunnerFingerprint =>
        val toRun = () => moduleOf[Runners](t.fullyQualifiedName()).Runners
        JavaTestTask(toRun, t)
    }

  private def moduleOf[A](classToLoad: String): A =
    testClassLoader.loadClass(s"$classToLoad$$").getDeclaredField("MODULE$").get(null).asInstanceOf[A]
}

case class JavaTestTask(toRun: () => Seq[TestRunner], taskDef: TaskDef) extends Task {
  override def tags(): Array[String] = Array()

  // TODO better error handling.
  // TODO how do we use the loggers? are they needed? We could hook them in as observers?
  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    // TODO we need the completion observer to be the "done" message
    Try(toRun()).map(r => JavaTest.run(CollectionConverters.toJava(r), Collections.emptyList[TestRunCompletionObserver]())) match {
      case Success(results) =>
        val status =
        if(!results.succeeded)
          Status.Failure
        else if(results.pendingCount > 0)
          Status.Pending
        else
          Status.Success
        eventHandler.handle(createEvent(status))
      case Failure(error) =>
        eventHandler.handle(createEvent(Status.Error, error = Some(error)))
    }
    Array()
  }

  // TODO do we need an event per test???? Or per suite :/
  private def createEvent(eventStatus: Status, error: Option[Throwable] = None): Event  = new Event {
    override def fullyQualifiedName(): String = taskDef.fullyQualifiedName()
    override def fingerprint(): Fingerprint = taskDef.fingerprint()
    override def selector(): Selector = taskDef.selectors().head // TODO what if the number of selectors != 1?
    override def status(): Status = eventStatus
    override def throwable(): OptionalThrowable = error.fold(new OptionalThrowable())(new OptionalThrowable(_))
    override def duration(): Long = -1
  }

}
