package io.github.olib963.javatest_scala

import io.github.olib963.javatest.TestRunner
import io.github.olib963.javatest.fixtures.{FixtureDefinition, Fixtures, Try => JTry}

import scala.util.{Failure, Success, Try}

trait FixtureSyntax {

  def fixture[Fixture](create: => Try[Fixture]): FixtureDefinition[Fixture] = destructibleFixture(create)(_ => Success(()))

  def destructibleFixture[Fixture](create: => Try[Fixture])(destroy: Fixture => Try[Unit]): FixtureDefinition[Fixture] =
    Fixtures.definitionFromFunctions(() => toInternalTry(create), (f: Fixture) => toInternalTry[Void](destroy(f).map(_ => null)))

  def fixtureRunner[Fixture](name: String, fixtureDefinition: FixtureDefinition[Fixture])(runnerFn: Fixture => TestRunner): TestRunner =
    Fixtures.fixtureRunner(name, fixtureDefinition, f => runnerFn(f))

  private def toInternalTry[A](scala: Try[A]): JTry[A] = scala match {
    case Success(a) => JTry.Success(a)
    case Failure(e) => JTry.Failure(e.getMessage)
  }
}
