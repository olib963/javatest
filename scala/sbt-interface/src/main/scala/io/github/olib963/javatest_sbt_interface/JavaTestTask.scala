package io.github.olib963.javatest_sbt_interface

import java.io.{OutputStream, PrintStream}
import java.util.concurrent.atomic.AtomicReference

import io.github.olib963.javatest._
import io.github.olib963.javatest_scala.run
import sbt.testing._

import scala.util.{Failure, Success, Try}


class JavaTestTask(val toRun: Try[Seq[TestRunner]], val taskDef: TaskDef, val totalResults: AtomicReference[TestResults]) extends Task {

  override def tags(): Array[String] = Array()

  import FunctionConverters._

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
    // TODO pass run config in
    val runConfig = RunConfiguration.empty()
      .addTestObservers(CollectionConverters.toJava(logResults(loggers)))
      .addRunObserver(addToTotal)
      .addRunObserver(triggerEvent(eventHandler))
    toRun match {
      case Success(runners) => run(runners, runConfig)
      case Failure(error) => eventHandler.handle(TestEvent(taskDef, Status.Error, error))
    }
    Array()
  }

  // The results of each run are added on to the total so that the "done()" message contains the total
  private val addToTotal: TestRunCompletionObserver = (results: TestResults) => {
    totalResults.getAndAccumulate(results, (r1: TestResults, r2: TestResults) => r1 combine r2)
    ()
  }

  // TODO log failures as error
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
    eventHandler.handle(TestEvent(taskDef, status))
  }

}
