(ns coke.facts-test
  (:require [clojure.test :refer [deftest is]]
            [coke.facts :as facts]))

(deftest catalog-structure
  "Catalog has jurisdiction entries with requirements."
  (is (seq facts/catalog) "Catalog should not be empty")
  (is (contains? facts/catalog :JPN) "Should have Japan entry")
  (is (contains? facts/catalog :USA) "Should have USA entry")
  (is (contains? facts/catalog :GBR) "Should have UK entry")
  (is (contains? facts/catalog :KOR) "Should have South Korea entry")
  (is (contains? facts/catalog :DEU) "Should have Germany entry")
  (is (contains? facts/catalog :POL) "Should have Poland entry"))

(deftest kor-requirements
  "South Korea has a real but honestly narrower requirement set than
  JPN/USA/GBR -- emissions-monitoring and worker-safety only."
  (let [reqs (facts/requirement-citations :KOR)]
    (is (seq reqs) "South Korea should have requirements")
    (is (contains? reqs :emissions-monitoring) "Should require emissions monitoring")
    (is (contains? reqs :worker-safety) "Should require worker safety")
    (is (not (contains? reqs :raw-material-verification))
      "Should NOT claim a raw-material-verification requirement that was not verified")
    (is (every? :spec-basis (vals reqs))
      "Every requirement should have an official spec-basis citation")))

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
    (is (= (:implemented cov) 6) "Should have 6 jurisdictions")
    (is (> (:coverage-pct cov) 0) "Coverage should be > 0%")))

(deftest deu-requirements
  "Germany has a real but honestly narrower requirement set than
  JPN/USA/GBR -- emissions-monitoring and worker-safety only."
  (let [reqs (facts/requirement-citations :DEU)]
    (is (seq reqs) "Germany should have requirements")
    (is (contains? reqs :emissions-monitoring) "Should require emissions monitoring")
    (is (contains? reqs :worker-safety) "Should require worker safety")
    (is (not (contains? reqs :raw-material-verification))
      "Should NOT claim a raw-material-verification requirement that was not verified")
    (is (every? :spec-basis (vals reqs))
      "Every requirement should have an official spec-basis citation")))

(deftest pol-requirements
  "Poland has a real but honestly narrower requirement set than
  JPN/USA/GBR -- emissions-monitoring and worker-safety only."
  (let [reqs (facts/requirement-citations :POL)]
    (is (seq reqs) "Poland should have requirements")
    (is (contains? reqs :emissions-monitoring) "Should require emissions monitoring")
    (is (contains? reqs :worker-safety) "Should require worker safety")
    (is (not (contains? reqs :raw-material-verification))
      "Should NOT claim a raw-material-verification requirement that was not verified")
    (is (every? :spec-basis (vals reqs))
      "Every requirement should have an official spec-basis citation")))

(deftest pol-evidence-satisfaction
  "Complete evidence satisfies Poland's requirements; incomplete evidence fails."
  (let [complete {:integrated-permit true
                  :installation-classification-record true
                  :risk-assessment-record true
                  :safety-plan true}
        incomplete {:integrated-permit true}]
    (is (facts/required-evidence-satisfied? :POL complete)
      "Complete evidence should satisfy Poland requirements")
    (is (not (facts/required-evidence-satisfied? :POL incomplete))
      "Incomplete evidence should fail Poland requirements")))
