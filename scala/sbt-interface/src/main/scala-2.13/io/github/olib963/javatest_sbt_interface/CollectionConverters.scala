package io.github.olib963.javatest_sbt_interface

import scala.jdk.CollectionConverters._

private [javatest_sbt_interface] object CollectionConverters {

  def toJava[A](seq: Seq[A]): java.util.List[A] = seq.asJava

}
