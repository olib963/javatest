package io.github.olib963.javatest_scala

object MyTests extends JavaTestSyntax {

  def main(args: Array[String]): Unit = {
    val results = run(SimpleTests, MatcherTests)

    if(!results.succeeded) {
      sys.error("Tests failed")
    }
  }

}
