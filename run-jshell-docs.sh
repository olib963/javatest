#!/bin/bash

set -e

version="$1"

echo "Building core jar"
mvn -pl :javatest-core package

echo "Finding JAR location"
JAVATEST_BUILD_DIR=$(mvn -pl :javatest-core help:evaluate -Dexpression=project.build.directory -q -DforceStdout)
JAVATEST_BUILD_NAME=$(mvn -pl :javatest-core help:evaluate -Dexpression=project.build.finalName -q -DforceStdout)
export ABSOLUTE_PATH_TO_JAVATEST_JAR="${JAVATEST_BUILD_DIR}/${JAVATEST_BUILD_NAME}.jar"

echo "Creating startup script"
envsubst '$ABSOLUTE_PATH_TO_JAVATEST_JAR' < javatest/javatest-core/docs/jshell/startup-script-template.jsh > javatest/javatest-core/docs/jshell/startup-script.jsh

mkdir -p javatest/javatest-core/docs/jshell/output

echo "Running JShell commands"
# Start JShell with the generated start up commands. Run each line from the script. Remove colour codes from output i.e. Green/Red/Reset & pipe to output file
jshell --startup DEFAULT --startup javatest/javatest-core/docs/jshell/startup-script.jsh \
  < javatest/javatest-core/docs/jshell/doc-script.jsh  \
  | sed -r "s/\x1B\[([0-9]{1,2}(;[0-9]{1,2})?)?[mGK]//g" \
  > javatest/javatest-core/docs/jshell/output/jshell-output
