version := "0.1"
name := "Test"

val scala13 = "2.13.1"
val scala12 = "2.12.10"
val scala11 = "2.11.12"

crossScalaVersions := Seq(scala11, scala12, scala13)

resolvers += Resolver.sonatypeRepo("snapshots")

javatestScalacheckVersion := Some("1.14.2") // TODO how do we want to configure this? Perhaps an ADT of NoScalaCheck | Defaults | Provided?

scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, scalaMajor)) if scalaMajor == 11 =>
    // Scala 11 has trouble with static functions on interfaces. Not sure why we need to set this option but the compiler tells us to.
    Seq("-target:jvm-1.8")
  case _ => Seq.empty
})