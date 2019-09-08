package io.github.olib963.scavatest

object MyTests extends JavaTestSyntax {

  def main(args: Array[String]): Unit = {
    val results = run(SimpleTestSuite)

    if(!results.succeeded) {
      sys.error("Tests failed")
    }
  }

}
