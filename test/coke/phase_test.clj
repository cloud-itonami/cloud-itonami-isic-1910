(ns coke.phase-test
  (:require [clojure.test :refer [deftest is]]
            [coke.phase :as phase]))

(deftest phase-table-structure
  "Phase table defines graph topology."
  (is (map? phase/phase-table) "Phase table should be a map")
  (is (contains? phase/phase-table :start) "Should have start node")
  (is (contains? phase/phase-table :nodes) "Should have nodes")
  (is (contains? phase/phase-table :edges) "Should have edges"))

(deftest starting-node
  "Can retrieve entry point."
  (is (= (phase/starting-node) :advisor)
    "Starting node should be :advisor"))

(deftest node-constants
  "Node constants are defined."
  (is (= phase/ADVISOR-NODE :advisor))
  (is (= phase/GOVERNOR-NODE :governor))
  (is (= phase/HOLD-NODE :hold))
  (is (= phase/COMPLETE-NODE :complete)))

(deftest terminal-node-classification
  "Can identify terminal nodes."
  (is (phase/is-terminal? :hold) "HOLD should be terminal")
  (is (phase/is-terminal? :complete) "COMPLETE should be terminal")
  (is (not (phase/is-terminal? :advisor)) "ADVISOR should not be terminal")
  (is (not (phase/is-terminal? :governor)) "GOVERNOR should not be terminal"))
