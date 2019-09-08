package io.github.olib963.javatest_scala.documentation

import java.io.FileReader

import io.github.olib963.javatest.TestRunner
import io.github.olib963.javatest.fixtures.FixtureDefinition
import io.github.olib963.javatest_scala._

import scala.util.{Success, Try}

object FixtureDocumentation extends FixtureSyntax with JavaTestSyntax {

  // tag::simple[]
  val mySimpleFixture: FixtureDefinition[String] = fixture(Success("Hello"))

  val runnerUsingSimpleFixture = fixtureRunner("sting fixture", mySimpleFixture)(word =>
    test("Simple fixture")(that(word, isEqualTo("Hello")))
  )
  // end::simple[]

  // tag::complex[]
  val myComplexFixture: FixtureDefinition[FileReader] =
    destructibleFixture(Try(new FileReader("myTestFile.txt")))(r => Try(r.close()))

  // You can reuse fixture runners then later apply the function to create your tests
  val reusableFixture: (FileReader => TestRunner) => TestRunner =
    fixtureRunner("test file reader", myComplexFixture)

  val runner1 = reusableFixture { fileReader =>
    test("Content reading"){
      val builder = new StringBuilder
      var c = fileReader.read()
      while (c != -1) {
        builder.append(c.toChar)
        c = fileReader.read()
      }
      that("Contents read from test file", builder.toString, isEqualTo("Hello, test!"))
    }
  }

  val runner2 = reusableFixture { aDifferentReader =>
    test("Character reading")(
      that("First character read from file", aDifferentReader.read().toChar, isEqualTo('H')))
  }
  // end::complex[]
}
