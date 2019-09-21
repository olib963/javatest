package io.github.olib963.javatest_scala

import io.github.olib963.javatest.matchers.{Matcher, StringMatchers}

import scala.collection.{GenTraversableOnce, SeqLike}
import scala.reflect.ClassTag
import scala.util.Try

trait MatcherSyntax {

  // The "that" function _has_ to be defined in JavaTestSyntax so we require both traits
  this: JavaTestSyntax =>

  // Default Matchers
  def isEqualTo[A](value: A): Matcher[A] = Matcher.isEqualTo(value)
  def hasType[T](implicit expectedClass: ClassTag[T]): Matcher[Any] = Matcher.hasType(expectedClass.runtimeClass)
  def isTheSameInstanceAs[A](instance: A): Matcher[A] = Matcher.isTheSameInstanceAs(instance)
  def not[A](matcher: Matcher[A]): Matcher[A] = Matcher.not(matcher)

  // String Matchers
  def startsWith(prefix: String): Matcher[String] = StringMatchers.startsWith(prefix)
  def endsWith(suffix: String): Matcher[String] = StringMatchers.endsWith(suffix)
  def containsString(subString: String): Matcher[String] = StringMatchers.containsString(subString)
  val isEmptyString: Matcher[String] = StringMatchers.isEmptyString
  val isBlankString: Matcher[String] = StringMatchers.isBlankString
  def isEqualToIgnoringCase(expected: String): Matcher[String] = StringMatchers.isEqualToIgnoringCase(expected)
  def hasLength(length: Int): Matcher[String] = StringMatchers.hasLength(length)

  // Scala matchers
  // TODO covariance issues. It would be great if we could just do "val isEmpty: Matcher[Option[Nothing]]"
  // TODO need nested matchers e.g. isSuccessThat(containsString("foo"))
  def isDefined[A]: Matcher[Option[A]] = Matcher.fromFunctions(o => o.isDefined, "be defined")
  def isEmptyOption[A]: Matcher[Option[A]] = Matcher.fromFunctions(o => o.isEmpty, "be empty")
  def optionContains[A](element: A): Matcher[Option[A]] = Matcher.fromFunctions(c => c.contains(element), s"contain {$element}")
  def isEmpty[A]: Matcher[GenTraversableOnce[A]] = Matcher.fromFunctions(o => o.isEmpty, "be empty")
  def contains[A](element: A): Matcher[SeqLike[A, _]] = Matcher.fromFunctions(c => c.contains(element), s"contain {$element}")
  def hasSize[A](size: Int): Matcher[Iterable[A]] = Matcher.fromFunctions(c => c.size == size, s"have size {$size}")
  def isSuccess[A]: Matcher[Try[A]] = Matcher.fromFunctions(t => t.isSuccess, s"ba a success")
  def isFailure[A]: Matcher[Try[A]] = Matcher.fromFunctions(t => t.isFailure, s"be a failure")

}
