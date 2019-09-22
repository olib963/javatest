#!/bin/bash

set -e

# Test and install java jars locally
mvn -P local-install install

# Test and install scala jars locally. Test of the core is performed by executing a main function in the test directory
cd scala
sbt ";+core/test:run;+publishLocal"

# Run the test project that makes use of the sbt plugin
cd sbt-test
sbt +test
