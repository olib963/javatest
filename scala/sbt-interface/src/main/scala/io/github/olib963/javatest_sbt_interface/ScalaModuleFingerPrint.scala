package io.github.olib963.javatest_sbt_interface

import sbt.testing.SubclassFingerprint

import scala.reflect.ClassTag

class ScalaModuleFingerPrint[A](implicit classTag: ClassTag[A]) extends SubclassFingerprint {
  override def isModule: Boolean = true
  override def requireNoArgConstructor(): Boolean = true
  override def superclassName(): String = classTag.runtimeClass.getName
  override def equals(obj: Any): Boolean = obj match {
    case classBased: SubclassFingerprint =>
      classBased.isModule && classBased.requireNoArgConstructor() && classBased.superclassName() == this.superclassName()
    case _ => false
  }

  override def toString: String = s"Fingerprint for ${superclassName()}"
}
