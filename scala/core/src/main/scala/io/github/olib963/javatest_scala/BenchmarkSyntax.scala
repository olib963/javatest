package io.github.olib963.javatest_scala

import java.time.{Duration => JavaDuration}

import io.github.olib963.javatest.benchmark.Benchmark
import io.github.olib963.javatest.{TestRunner, Testable}

import scala.concurrent.duration.Duration
import scala.collection.JavaConverters._

trait BenchmarkSyntax {

  def benchmark(test: Testable.Test)(implicit formatter: DurationFormat): Testable.Test =
    Benchmark.benchmark(test, (j: JavaDuration) => formatter(toScala(j)))

  def benchmark(test: Testable.TestSuite)(implicit formatter: DurationFormat): Testable.TestSuite =
    Benchmark.benchmark(test, (j: JavaDuration) => formatter(toScala(j)))

  def benchmark(test: Testable)(implicit formatter: DurationFormat): Testable =
    Benchmark.benchmark(test, (j: JavaDuration) => formatter(toScala(j)))

  def benchmark(all: Seq[Testable])(implicit formatter: Duration => String): Seq[Testable] = all.map(benchmark)

  def benchmark(runners: TestRunner*)(implicit formatter: DurationFormat): TestRunner =
    Benchmark.benchmark(runners.asJava.stream(), (j: JavaDuration) => formatter(toScala(j)))

  def failIfLongerThan(duration: Duration)(test: Testable.Test)(implicit formatter: DurationFormat): Testable.Test =
    Benchmark.failIfLongerThan(toJava(duration), test, (j: JavaDuration) => formatter(toScala(j)))

  private def toScala(duration: JavaDuration) = Duration.fromNanos(duration.toNanos)
  private def toJava(duration: Duration) = JavaDuration.ofNanos(duration.toNanos)

}

trait DurationFormat extends (Duration => String)

object DurationFormat {
  implicit val formatter: DurationFormat = d => s"${d.toSeconds}s ${d.toMillis % 1000}ms"
}
