(ns javatest-clj.tests
  (:require [javatest-clj.core :refer :all])
  (:gen-class))

; lein run -m javatest-clj.tests
(defn -main []
  (let [simple-test (Test "Simple test"
                          (that true "passes"))
        suite (Suite "My suite"
                     [(Suite "Nested suite" [simple-test])
                      (Test "another test"
                            (that (= 2 (+ 1 1))))])
        results (run-tests suite)]
    (when-not (:succeeded results)
      (throw (Exception. "BOO TESTS FAILED")))))
