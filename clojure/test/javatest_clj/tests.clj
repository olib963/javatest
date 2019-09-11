(ns javatest-clj.tests
  (:import io.github.olib963.javatest.JavaTest
           io.github.olib963.javatest.CheckedSupplier)
  (:gen-class))

; lein run -m javatest-clj.tests
(defn -main []
  (let [simple-test (JavaTest/test "Simple test" (reify CheckedSupplier
                                                   (get [this] (JavaTest/that true "passes"))))
        results (JavaTest/runTests simple-test)]
    (if (.succeeded results)
      (println "yay")
      (throw (Exception. "BOO TESTS FAILED")))))
