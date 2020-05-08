package io.github.olib963.javatest_sbt_interface

import io.github.olib963.javatest_scala.{Runners, Suite}
import sbt.testing.{Fingerprint, Framework, Runner}

class JavaTestScalaFramework extends Framework {
  override def name(): String = "JavaTest-Scala"

  override def fingerprints(): Array[Fingerprint] = Array(
    JavaTestScalaFramework.SuiteFingerprint,
    JavaTestScalaFramework.RunnerFingerprint
  )

  override def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    new JavaTestRunner(args, remoteArgs, testClassLoader)
}

object JavaTestScalaFramework {
  val SuiteFingerprint = new ScalaModuleFingerPrint[Suite]
  val RunnerFingerprint = new ScalaModuleFingerPrint[Runners]
}
