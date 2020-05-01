val javaTestVersion = "0.3.0-SNAPSHOT"
val organisationName = "io.github.olib963"

val scala13 = "2.13.1"
val scala12 = "2.12.10"
val scala11 = "2.11.12"

val SettingsForAllProjects = Seq(
  organization := organisationName,
  version := javaTestVersion,
)

val CommonSettings = Seq(
  scalacOptions ++= Seq(
    // Fail build on warnings
    "-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"
  ),
  crossScalaVersions := Seq(scala11, scala12, scala13),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/olib963/javatest/scala"),
      "scm:git@github.com:olib963/javatest.git"
    )
  ),
  developers := List(
    Developer(
      id    = "olib963",
      name  = "olib963",
      email = "",
      url   = url("https://github.com/olib963")
    )
  ),
  licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage := Some(url("https://github.com/olib963/javatest/scala")),
  publishTo := sonatypePublishToBundle.value,
  resolvers += Resolver.sonatypeRepo("snapshots"),
  scalacOptions ++= scalaVersionSpecificOptions(scalaVersion.value),
) ++ SettingsForAllProjects

def scalaVersionSpecificOptions(version: String): Option[String] =
  CrossVersion.partialVersion(version) match {
    case Some((2, scalaMajor)) if scalaMajor == 11 =>
      // Scala 11 has trouble with static functions on interfaces. Not sure why we need to set this option but the compiler tells us to.
      Some("-target:jvm-1.8")
    case _ => None
  }

val core = (project in file("core"))
  .settings(CommonSettings: _*)
  .settings(
    name := "javatest-scala",
    // sbt test:run
    mainClass in Test := Some("io.github.olib963.javatest_scala.MyTests"),
    libraryDependencies ++= Seq(
      organisationName % "javatest-core" % javaTestVersion,
      organisationName % "javatest-benchmark" % javaTestVersion,
      organisationName % "javatest-eventually" % javaTestVersion,
      organisationName % "javatest-fixtures" % javaTestVersion,
      organisationName % "javatest-matchers" % javaTestVersion
    ),
    description := "Scala wrapper around JavaTest framework"
  )

val scalaCheck = (project in file("scala-check"))
  .dependsOn(core)
  .settings(CommonSettings: _*)
  .settings(
    name := "javatest-scalacheck",
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.14.0",
    ),
    description := "Module to create assertions from scalacheck generators"
  )

val sbtInterface = (project in file("sbt-interface"))
  .dependsOn(core)
  .settings(CommonSettings: _*)
  .settings(
    name := "javatest-sbt-interface",
    libraryDependencies ++= Seq(
      "org.scala-sbt" % "test-interface" % "1.0"
    ),
    description := "Implementation of SBTs test interface for JavaTest"
  )

val sbtPlugin = (project in file("sbt-plugin"))
  .settings(CommonSettings: _*)
  .dependsOn(sbtInterface)
  .enablePlugins(SbtPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "sbt-javatest",
    // The build info plugin generates an object javatest_sbt.BuildInfo that contains the version defined in build.sbt
    buildInfoKeys := Seq[BuildInfoKey]("javaTestVersion" -> javaTestVersion),
    buildInfoPackage := "io.github.olib963.javatest_sbt",
    // Sbt plugins cannot be built on multiple scala versions, sbt 1.3.x uses scala 2_12
    crossScalaVersions := Seq(scala12),
    description := "SBT plugin to automate setup of JavaTest with SBT"
  )

// It seems in order to create a cross compiled SBT project you need to aggregate sub projects into a root. This seems to
// stop the default scala 2.12.8 publishing of every project that otherwise occurs
lazy val root = (project in file("."))
  .aggregate(core, scalaCheck, sbtInterface, sbtPlugin)
  .settings(SettingsForAllProjects: _*)
  .settings(
    name := "javatest-scala-aggregate",
    // crossScalaVersions must be set to Nil on the aggregating project.
    crossScalaVersions := Nil,
    // We do not want to publish this project
    skip in publish := true
  )
