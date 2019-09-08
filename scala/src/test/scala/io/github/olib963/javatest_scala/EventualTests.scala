package io.github.olib963.javatest_scala

import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicInteger

import io.github.olib963.javatest.Testable

import scala.concurrent.duration._

case class EventualTests(executorService: ExecutorService) extends Suite with JavaTestSyntax with EventuallySyntax {

  override def tests: Seq[Testable] =
    Seq(
      benchmark(test("Default config")(
        eventually(that(true, "always passes")))),

      failIfLongerThan(2.seconds)(
        test("custom config") {
          // tag::assertion[]
          import scala.concurrent.duration._
          implicit val config: EventuallyConfig = EventuallyConfig(attempts = 5, waitInterval = 500.millis)
          val atomicInt = new AtomicInteger(0)
          executorService.submit(() => {
            Thread.sleep(1000)
            atomicInt.getAndIncrement()
          })
          eventually(that(atomicInt.get(), isEqualTo(1)))
          // end::assertion[]
        })
    )
}
