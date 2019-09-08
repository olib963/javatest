name := "javatest-scala"
organization := "io.github.olib963"

// TODO auto increment this as part of the release process
// TODO add sbt publish to script with corresponding repo information
val javaTestVersion = "0.2.0-SNAPSHOT"

version := javaTestVersion

scalaVersion := "2.12.8"

// TODO cross build to multiple scala version

// TODO how do we remove this??? SBT seems to be ignoring local m2
resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies ++= Seq(
  "io.github.olib963" % "javatest-core" % javaTestVersion,
  "io.github.olib963" % "javatest-benchmark" % javaTestVersion,
  "io.github.olib963" % "javatest-eventually" % javaTestVersion,
  "io.github.olib963" % "javatest-fixtures" % javaTestVersion,
  "io.github.olib963" % "javatest-matchers" % javaTestVersion
)

// Fail build on warnings
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

// sbt test:run
mainClass in Test := Some("io.github.olib963.scavatest.MyTests")
