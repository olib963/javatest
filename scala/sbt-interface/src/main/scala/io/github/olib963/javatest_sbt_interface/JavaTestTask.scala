package io.github.olib963.javatest_sbt_interface

import java.io.{OutputStream, PrintStream}
import java.util.concurrent.atomic.AtomicReference

import io.github.olib963.javatest.TestResult.{SingleTestResult, SuiteResult}
import io.github.olib963.javatest._
import io.github.olib963.javatest_scala.run
import sbt.testing._

import scala.util.{Failure, Success, Try}


class JavaTestTask(val toRun: Try[Seq[TestRunner]], val taskDef: TaskDef, val totalResults: AtomicReference[TestResults]) extends Task {

  override def tags(): Array[String] = Array()

  import FunctionConverters._

  override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
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

  // TODO clean up the logging abstraction
  private def logResults(loggers: Array[Logger]): Seq[TestCompletionObserver] = loggers.map { logger =>
    val passingLogger = TestCompletionObserver.logTo(logger.ansiCodesSupported(), new LogInfoStream(logger))
    val failingLogger = TestCompletionObserver.logTo(logger.ansiCodesSupported(), new LogErrorStream(logger))
    ((result: TestResult) => if(passed(result)) passingLogger.onTestCompletion(result) else failingLogger.onTestCompletion(result)): TestCompletionObserver
  }.toList

  private def passed(result: TestResult): Boolean = result.`match`(
    (suite: SuiteResult) => suite.results().allMatch((r: TestResult) => passed(r)),
    (test: SingleTestResult) => test.result.holds
  )

  // TODO what is the better abstraction? Should we create a JavaTest Logger?
  private abstract class LogWrapper(val logFn: String => Unit) extends PrintStream(OutputStream.nullOutputStream(), true) {
    private var internalBuffer: String = ""
    override def print(s: String): Unit = {
      internalBuffer = internalBuffer + s
    }

    override def println(x: String): Unit = {
      logFn(internalBuffer + x)
      internalBuffer = ""
    }
  }

  private class LogInfoStream(logger: Logger) extends LogWrapper(logger.info)
  private class LogErrorStream(logger: Logger) extends LogWrapper(logger.error)

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
