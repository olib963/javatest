(ns javatest-clj.core
  (:import (io.github.olib963.javatest JavaTest CheckedSupplier)))

(defmacro  ^:private obj->map
  [o & bindings]
  (let [s (gensym "local")]
    `(let [~s ~o]
       ~(->> (partition 2 bindings)
             (map (fn [[k v]]
                    (if (vector? v)
                      [k (list (last v) (list (first v) s))]
                      [k (list v s)])))
             (into {})))))

;; TODO should we instead create maps then map->obj them at the point of run?
(defn Suite [name tests]
  (JavaTest/suite name tests))

(defmacro Test
  [name body]
  `(JavaTest/test
     ~name
     (reify CheckedSupplier
       (get [_] ~body))))

(defmacro that
  ([predicate]
    ;; TODO should we use a matcher for this instead? It may be a useful expansion of (that (pred? stuff)) to (that "something" stuff (matcher ~predicate '~predicate))?
    `(that ~predicate (str "expected " '~predicate)))
  ([condition description]
   `(JavaTest/that ~condition ~description)))

(defn- stream->seq [stream]
  (-> stream
      .iterator
      iterator-seq))

(defn- results->map [results]
  (obj->map results
            :succeeded .succeeded
            :passed .successCount
            :failed .failureCount
            :pending .pendingCount
            :logs [.allLogs #(stream->seq %)]
            :results [.allResults #(stream->seq %)]))

(defn run-tests [tests]
  (results->map (JavaTest/runTests tests)))


