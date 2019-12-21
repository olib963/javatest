package io.github.olib963.javatest_scala.scalacheck

import io.github.olib963.javatest.{Assertion, AssertionResult}
import org.scalacheck.{Arbitrary, Gen}

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

private [scalacheck] class GeneratorAssertion[A](gen: Gen[A], f: A => Assertion) extends Assertion {
  override def run(): AssertionResult = {
    // TODO look into how to run Properties in scalacheck
    f(gen.sample.get).run()
  }
}
