(ns coke.facts-test
  (:require [clojure.test :refer [deftest is]]
            [coke.facts :as facts]))

(deftest catalog-structure
  "Catalog has jurisdiction entries with requirements."
  (is (seq facts/catalog) "Catalog should not be empty")
  (is (contains? facts/catalog :JPN) "Should have Japan entry")
  (is (contains? facts/catalog :USA) "Should have USA entry")
  (is (contains? facts/catalog :GBR) "Should have UK entry"))

(deftest jpn-requirements
  "Japan has required evidence checklist."
  (let [reqs (facts/requirement-citations :JPN)]
    (is (seq reqs) "Japan should have requirements")
    (is (contains? reqs :raw-material-verification) "Should require material verification")
    (is (contains? reqs :emissions-monitoring) "Should require emissions monitoring")
    (is (contains? reqs :worker-safety) "Should require worker safety")))

(deftest evidence-satisfaction
  "Check if evidence satisfies jurisdiction requirements."
  (let [evidence {:supplier-license true
                  :coal-analysis-report true
                  :quality-cert true
                  :emissions-baseline true
                  :monitoring-equipment-cert true
                  :safety-plan true
                  :worker-training-records true
                  :storage-facility-cert true
                  :hazmat-procedure true}]
    (is (facts/required-evidence-satisfied? :JPN evidence)
      "Complete evidence should satisfy Japan requirements")))

(deftest evidence-incomplete
  "Incomplete evidence fails requirement check."
  (let [incomplete-evidence {:supplier-license true}]
    (is (not (facts/required-evidence-satisfied? :JPN incomplete-evidence))
      "Incomplete evidence should fail")))

(deftest coverage-reporting
  "Coverage report reflects catalog size."
  (let [cov (facts/coverage)]
    (is (contains? cov :implemented) "Should report implemented count")
    (is (contains? cov :worldwide-jurisdictions) "Should report worldwide jurisdictions")
    (is (contains? cov :coverage-pct) "Should report coverage percentage")
    (is (= (:implemented cov) 3) "Should have 3 jurisdictions")
    (is (> (:coverage-pct cov) 0) "Coverage should be > 0%")))
