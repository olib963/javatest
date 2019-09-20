# Make sure that test, testOnly and testQuick work
# TODO test quick will not currently only run failing tests but will instead run all tests if _any_ fail
sbt ";test;testOnly my.app.ObjectSuite;testOnly *ObjectRunners"
