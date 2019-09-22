package io.github.olib963.javatest_scala

import java.time.{Duration => JavaDuration}

import io.github.olib963.javatest.Assertion
import io.github.olib963.javatest.eventually.{EventualConfig, Eventually}

import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import FunctionConverters._

trait EventuallySyntax {

  def eventually(fn: => Assertion)(implicit config: EventuallyConfig) = {
    val interval = EventualConfig.of(config.attempts, toJava(config.waitInterval))
    val javaConfig = config.initialDelay.fold(interval)(delay => interval.withInitialDelay(toJava(delay)))
    Eventually.eventually(() => fn, javaConfig)
  }

  private def toJava(duration: Duration) = JavaDuration.ofNanos(duration.toNanos)

}

case class EventuallyConfig(attempts: Int, waitInterval: Duration, initialDelay: Option[Duration] = None)

object EventuallyConfig {
  implicit val default: EventuallyConfig = EventuallyConfig(1, 100.millis)
}
