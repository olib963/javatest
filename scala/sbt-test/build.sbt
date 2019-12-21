version := "0.1"
name := "Test"

val scala13 = "2.13.1"
val scala12 = "2.12.10"
val scala11 = "2.11.12"

crossScalaVersions := Seq(scala11, scala12, scala13)

javatestScalacheckVersion := Some("1.14.2")
