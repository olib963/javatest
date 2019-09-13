package io.github.olib963.javatest_sbt

import sbt.testing.{Fingerprint, Framework, Runner}

class JavaTestScalaFramework extends Framework {
  override def name(): String = ???

  override def fingerprints(): Array[Fingerprint] = ???

  override def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner = ???
}
