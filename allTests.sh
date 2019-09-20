mvn -P local-install install && cd scala && sbt ";core/test:run;publishLocal" && cd sbt-test && sbt test
