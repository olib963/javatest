val javaTestVersion = "0.2.0-SNAPSHOT"
val organisationName = "io.github.olib963"

val CommonSettings = Seq(
  organization := organisationName,
  version := javaTestVersion,
  scalaVersion := "2.12.8",
  libraryDependencies ++= Seq(
    organisationName % "javatest-core" % javaTestVersion,
    organisationName % "javatest-benchmark" % javaTestVersion,
    organisationName % "javatest-eventually" % javaTestVersion,
    organisationName % "javatest-fixtures" % javaTestVersion,
    organisationName % "javatest-matchers" % javaTestVersion
  ),
  // Fail build on warnings
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
)

val core = (project in file("core"))
  .settings(CommonSettings: _*)
  .settings(
    name := "javatest-scala",
    // sbt test:run
    mainClass in Test := Some("io.github.olib963.javatest_scala.MyTests"),
  )

val sbtIntegration = (project in file("sbt"))
  .dependsOn(core)
  .settings(CommonSettings: _*)
  .settings(
    name := "javatest-sbt",
    libraryDependencies += "org.scala-sbt" % "test-interface" % "1.0"
  )
