package io.github.olib963.javatest_scala

import io.github.olib963.javatest.matchers.Matcher
import io.github.olib963.javatest.matchers.StringMatchers

import scala.reflect.ClassTag

trait MatcherSyntax {

  // The "that" function _has_ to be defined in JavaTestSyntax so we require both traits
  this: JavaTestSyntax =>

  // TODO need scala specific matchers

  // Default Matchers
  def isEqualTo[A](value: A): Matcher[A] = Matcher.isEqualTo(value)
  def hasType[T](implicit expectedClass: ClassTag[T]): Matcher[Any] = Matcher.hasType(expectedClass.runtimeClass)
  def isTheSameInstanceAs[A](instance: A): Matcher[A] = Matcher.isTheSameInstanceAs(instance)

  // String Matchers
  def startsWith(prefix: String): Matcher[String] = StringMatchers.startsWith(prefix)
  def endsWith(suffix: String): Matcher[String] = StringMatchers.endsWith(suffix)
  def containsString(subString: String): Matcher[String] = StringMatchers.containsString(subString)
  val isEmptyString: Matcher[String] = StringMatchers.isEmptyString
  val isBlankString: Matcher[String] = StringMatchers.isBlankString
  def isEqualToIgnoringCase(expected: String): Matcher[String] = StringMatchers.isEqualToIgnoringCase(expected)
  def hasLength(length: Int): Matcher[String] = StringMatchers.hasLength(length)

}
