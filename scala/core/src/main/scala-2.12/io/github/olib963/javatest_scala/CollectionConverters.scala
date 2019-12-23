package io.github.olib963.javatest_scala

import scala.collection.JavaConverters._

private [javatest_scala] object CollectionConverters {

  def toJava[A](seq: Seq[A]): java.util.List[A] = seq.asJava

}
