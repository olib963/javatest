package javatest_sbt

import io.github.olib963.javatest_sbt_interface.JavaTestScalaFramework
import sbt.Keys.{libraryDependencies, testFrameworks}
import sbt.librarymanagement.LibraryManagementSyntax
import sbt.{AutoPlugin, Setting, TestFramework}

object EnableJavaTestPlugin extends AutoPlugin with LibraryManagementSyntax {

  override def trigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = Seq(
    libraryDependencies += "io.github.olib963" %% "javatest-sbt-interface" % BuildInfo.javaTestVersion % Test,
    testFrameworks += new TestFramework(classOf[JavaTestScalaFramework].getName),
  )

}
