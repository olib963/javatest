val javaTestVersion = "0.2.0-SNAPSHOT"
val organisationName = "io.github.olib963"

val scala13 = "2.13.1"
val scala12 = "2.12.10"
val scala11 = "2.11.12"

val CommonSettings = Seq(
  organization := organisationName,
  version := javaTestVersion,
  libraryDependencies ++= Seq(
    organisationName % "javatest-core" % javaTestVersion,
    organisationName % "javatest-benchmark" % javaTestVersion,
    organisationName % "javatest-eventually" % javaTestVersion,
    organisationName % "javatest-fixtures" % javaTestVersion,
    organisationName % "javatest-matchers" % javaTestVersion
  ),
  scalacOptions ++= Seq(
    // Fail build on warnings
    "-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"
  ),
  crossScalaVersions := Seq(scala11, scala12, scala13)
)

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
    scalacOptions ++= scalaVersionSpecificOptions(scalaVersion.value)
  )

val sbtInterface = (project in file("sbt-interface"))
  .dependsOn(core)
  .settings(CommonSettings: _*)
  .settings(
    name := "javatest-sbt-interface",
    libraryDependencies += "org.scala-sbt" % "test-interface" % "1.0"
  )

val sbtPlugin = (project in file("sbt-plugin"))
  .settings(CommonSettings: _*)
  .dependsOn(sbtInterface)
  .enablePlugins(SbtPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "javatest-sbt",
    // The build info plugin generates an object javatest_sbt.BuildInfo that contains the version defined in build.sbt
    buildInfoKeys := Seq[BuildInfoKey]("javaTestVersion" -> javaTestVersion),
    buildInfoPackage := "io.github.olib963.javatest_sbt",
    // Sbt plugins cannot be built on multiple scala versions, sbt 1.3.x uses scala_12 and that's it. I have decided since the plugin is
    // scala version agnostic to just not publish it as a versioned artifact. It automatically looks for the version of javatest-sbt-interface
    // for the clients scala version
    crossPaths := false,
    crossScalaVersions := Seq(scala12)
  )

// It seems in order to create a cross compiled SBT project you need to aggregate sub projects into a root. This seems to
// stop the default scala 2.12.8 publishing of every project that otherwise occurs
lazy val root = (project in file("."))
  .aggregate(core, sbtInterface, sbtPlugin)
  .settings(
    // crossScalaVersions must be set to Nil on the aggregating project.
    crossScalaVersions := Nil,
    // We do not want to publish this project
    skip in publish := true
  )
