(def javatest-version "0.2.0-SNAPSHOT")

(defproject javatest-clj javatest-version
  :description "Clojure wrapper for JavaTest"
  :url "https://github.com/olib963/javatest/clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.github.olib963/javatest-core ~javatest-version]
                 [io.github.olib963/javatest-benchmark ~javatest-version]
                 [io.github.olib963/javatest-eventually ~javatest-version]
                 [io.github.olib963/javatest-fixtures ~javatest-version]
                 [io.github.olib963/javatest-matchers ~javatest-version]])
