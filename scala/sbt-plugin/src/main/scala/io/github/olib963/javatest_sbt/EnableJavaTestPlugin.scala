package io.github.olib963.javatest_sbt

import io.github.olib963.javatest_sbt_interface.JavaTestScalaFramework
import sbt.Keys.{libraryDependencies, testFrameworks}
import sbt.librarymanagement.LibraryManagementSyntax
import sbt._

object EnableJavaTestPlugin extends AutoPlugin with LibraryManagementSyntax {

  object autoImport {
    // configure the version of scalacheck to include on the test classpath
    val javatestScalacheckVersion = settingKey[Option[String]]("The version of scalacheck to use if scalacheck integration is required. Muse be 1.14.x")
    lazy val baseOSettings: Seq[Def.Setting[_]] = Seq(javatestScalacheckVersion := None)
  }

  import autoImport._

  override def trigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = {
    Seq(
      libraryDependencies ++= {
        val scalacheckDependencies = javatestScalacheckVersion.value.fold(Seq.empty[ModuleID]){ version =>
          Seq(
            "org.scalacheck" %% "scalacheck" % version,
            "io.github.olib963" %% "javatest-scalacheck" % BuildInfo.javaTestVersion
          )
        }
        ("io.github.olib963" %% "javatest-sbt-interface" % BuildInfo.javaTestVersion +: scalacheckDependencies).map(_ % Test)
      },
      testFrameworks += new TestFramework(classOf[JavaTestScalaFramework].getName),
    )
  }

}
