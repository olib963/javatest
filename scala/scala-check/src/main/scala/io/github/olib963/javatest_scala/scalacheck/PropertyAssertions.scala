package io.github.olib963.javatest_scala.scalacheck

import java.io.{PrintWriter, StringWriter}

import io.github.olib963.javatest.{Assertion, AssertionResult}
import org.scalacheck.Prop.Arg
import org.scalacheck.Test.{Exhausted, Passed, PropException, Proved}
import org.scalacheck.util.Pretty
import org.scalacheck.{Arbitrary, Gen, Prop, Test}

trait PropertyAssertions {

  def forAll[A](test: A => Assertion)(implicit arb: Arbitrary[A]): Assertion = forAll(arb.arbitrary)(test)

  def forAll[A, B](test: (A, B) => Assertion)(implicit arbA: Arbitrary[A], arbB: Arbitrary[B]): Assertion = forAll(arbA.arbitrary, arbB.arbitrary)(test)

  def forAll[A, B, C](test: (A, B, C) => Assertion)(implicit arbA: Arbitrary[A], arbB: Arbitrary[B], arbC: Arbitrary[C]): Assertion = forAll(arbA.arbitrary, arbB.arbitrary, arbC.arbitrary)(test)

  def forAll[A, B, C, D](test: (A, B, C, D) => Assertion)(implicit arbA: Arbitrary[A], arbB: Arbitrary[B], arbC: Arbitrary[C], arbD: Arbitrary[D]): Assertion = forAll(arbA.arbitrary, arbB.arbitrary, arbC.arbitrary, arbD.arbitrary)(test)

  def forAll[A, B, C, D, E](test: (A, B, C, D, E) => Assertion)(implicit arbA: Arbitrary[A], arbB: Arbitrary[B], arbC: Arbitrary[C], arbD: Arbitrary[D], arbE: Arbitrary[E]): Assertion = forAll(arbA.arbitrary, arbB.arbitrary, arbC.arbitrary, arbD.arbitrary, arbE.arbitrary)(test)

  def forAll[A](gen: Gen[A])(test: A => Assertion): Assertion = new GeneratorAssertion(gen, test)

  def forAll[A, B](genA: Gen[A], genB: Gen[B])(test: (A, B) => Assertion): Assertion = {
    val gen = for {
      a <- genA
      b <- genB
    } yield (a, b)
    new GeneratorAssertion(gen, test.tupled)
  }

  def forAll[A, B, C](genA: Gen[A], genB: Gen[B], genC: Gen[C])(test: (A, B, C) => Assertion): Assertion = {
    val gen = for {
      a <- genA
      b <- genB
      c <- genC
    } yield (a, b, c)
    new GeneratorAssertion(gen, test.tupled)
  }

  def forAll[A, B, C, D](genA: Gen[A], genB: Gen[B], genC: Gen[C], genD: Gen[D])(test: (A, B, C, D) => Assertion): Assertion = {
    val gen = for {
      a <- genA
      b <- genB
      c <- genC
      d <- genD
    } yield (a, b, c, d)
    new GeneratorAssertion(gen, test.tupled)
  }

  def forAll[A, B, C, D, E](genA: Gen[A], genB: Gen[B], genC: Gen[C], genD: Gen[D], genE: Gen[E])(test: (A, B, C, D, E) => Assertion): Assertion = {
    val gen = for {
      a <- genA
      b <- genB
      c <- genC
      d <- genD
      e <- genE
    } yield (a, b, c, d, e)
    new GeneratorAssertion(gen, test.tupled)
  }

}

private[scalacheck] class GeneratorAssertion[A](gen: Gen[A], f: A => Assertion) extends Assertion {

  implicit val prettyArgs: Seq[Arg[Any]] => Pretty = Pretty.prettyArgs
  override def run(): AssertionResult = {
    // TODO look into how to run Properties in scalacheck properly
    val prop = Prop.forAllNoShrink(gen) { a =>
      val result = f(a).run()
      if(result.pending){
        Prop.exception(Pending(result.description))
      } else if(result.holds){
        Prop.passed
      } else {
        Prop.exception(Failed(result.description))
      }
    }
    val result = Test.check(prop)(identity) // TODO allow customisation of parameters
    result.status match {
      // TODO how to get the last result? Just in case we want to print what happens in the case we want to show the success message
      case Passed | _: Proved => AssertionResult.success(s"Passed ${result.succeeded} property checks.")
      case PropException(_, Pending(reason), _) => AssertionResult.pending(reason)
      case PropException(args, Failed(reason), _) =>
        AssertionResult.failure(s"$reason. Failed when passed arguments ${Pretty.pretty(args)}.")
      case Exhausted => AssertionResult.failure("Scalacheck could not generate enough values to meet parameter requirements")
      case PropException(_, exception: AssertionError, _) => AssertionResult.assertionThrown(exception)
      case PropException(args, exception, _) =>
        val stringWriter = new StringWriter
        stringWriter.append(s"Failed with exception when passed arguments ${Pretty.pretty(args)}.")
        stringWriter.append("\n")
        stringWriter.append("Message: ")
        stringWriter.append(exception.getMessage)
        stringWriter.append("\n")
        exception.printStackTrace(new PrintWriter(stringWriter))
        AssertionResult.failure(stringWriter.toString)
      case Test.Failed(args, _) => AssertionResult.failure(s"Failed when passed arguments ${Pretty.pretty(args)}.")
    }
  }

  sealed abstract class ScalacheckException() extends Exception("", null, false, false) // Suppressed stack trace
  case class Pending(reason: String) extends ScalacheckException
  case class Failed(reason: String) extends ScalacheckException

}
