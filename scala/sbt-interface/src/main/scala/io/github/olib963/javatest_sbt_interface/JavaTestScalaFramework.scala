package io.github.olib963.javatest_sbt_interface

import java.io.{OutputStream, PrintStream}
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors

import io.github.olib963.javatest.{JavaTest, TestCompletionObserver, TestResults, TestRunCompletionObserver, TestRunner}
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
      case t if t.fingerprint() == JavaTestScalaFramework.SuiteFingerprint =>
        JavaTestTask(moduleOf[Suite](t).map(Left(_)), t, allResults)
      case t if t.fingerprint() == JavaTestScalaFramework.RunnerFingerprint =>
        JavaTestTask(moduleOf[Runners](t).map(t => Right(t.Runners)), t, allResults)
    }

  private def moduleOf[A](task: TaskDef): Try[A] = Try {
    testClassLoader.loadClass(s"${task.fullyQualifiedName()}$$").getDeclaredField("MODULE$").get(null).asInstanceOf[A]
  }
}

case class JavaTestTask(toRun: Try[Either[Suite, Seq[TestRunner]]], taskDef: TaskDef, totalResults: AtomicReference[TestResults]) extends Task {
  override def tags(): Array[String] = Array()
  import FunctionConverters._

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    toRun match {
      case Success(Left(suite)) =>
        // TODO should all observers be passed in to the run function?
        val runner = JavaTest.testableRunner(CollectionConverters.toJava(Seq(suite)), CollectionConverters.toJava(logResults(loggers)))
        run(Seq(runner), eventHandler)
      case Success(Right(runners)) =>
        // TODO test observers aren't here.
        run(runners, eventHandler)
      case Failure(error) =>
        eventHandler.handle(createEvent(Status.Error, error = Some(error)))
    }
    Array()
  }

  private def run(runners: Seq[TestRunner], eventHandler: EventHandler): Unit =
    JavaTest.run(CollectionConverters.toJava(runners), CollectionConverters.toJava(List(addToTotal, triggerEvent(eventHandler))))

  // The results of each run are added on to the total so that the "done()" message contains the total
  private def addToTotal: TestRunCompletionObserver = (results: TestResults) => {
    totalResults.getAndAccumulate(results, (r1: TestResults, r2: TestResults) => r1 combine r2)
    ()
  }

  private def logResults(loggers: Array[Logger]): Seq[TestCompletionObserver] = loggers.map(logger =>
    TestCompletionObserver.logTo(logger.ansiCodesSupported(), new LogInfoStream(logger))
  ).toList

  // TODO what is the better abstraction? Should we create a JavaTest Logger?
  private class LogInfoStream(logger: Logger) extends PrintStream(OutputStream.nullOutputStream(), true) {
    private var internalBuffer: String = ""
    override def print(s: String): Unit = {
      internalBuffer = internalBuffer + s
    }

    override def println(x: String): Unit = {
      logger.info(internalBuffer + x)
      internalBuffer = ""
    }
  }

  // An event is triggered by each test result to inform sbt of failures & successes
  def triggerEvent(eventHandler: EventHandler): TestRunCompletionObserver = (results: TestResults) => {
    val status =
      if (!results.succeeded)
        Status.Failure
      else if (results.pendingCount > 0)
        Status.Pending
      else
        Status.Success
    eventHandler.handle(createEvent(status))
  }

  // TODO do we need an event per test???? Or per suite :/
  private def createEvent(eventStatus: Status, error: Option[Throwable] = None): Event = new Event {
    override def fullyQualifiedName(): String = taskDef.fullyQualifiedName()
    override def fingerprint(): Fingerprint = taskDef.fingerprint()
    override def selector(): Selector = taskDef.selectors().head // TODO what if the number of selectors != 1?
    override def status(): Status = eventStatus
    override def throwable(): OptionalThrowable = error.fold(new OptionalThrowable())(new OptionalThrowable(_))
    override def duration(): Long = -1
  }

}
