(ns coke.store-contract-test
  (:require [clojure.test :refer [deftest is]]
            [coke.store :as store]))

(deftest ^{:doc "In-memory store initializes with reference data."} mem-store-initialized
  (let [st (store/mem-store)]
    (is (contains? st :data) "Should have data atom")
    (is (map? @(:data st)) "Data should be a map")))

(deftest ^{:doc "Can retrieve supplier records."} supplier-access
  (let [st (store/mem-store)
        supplier (store/supplier st "supplier-001")]
    (is (some? supplier) "Should find supplier")
    (is (= (:name supplier) "Coal Mining Corp A") "Should have correct name")
    (is (:license-verified? supplier) "Should be license-verified")))

(deftest ^{:doc "Can retrieve material (coal batch) records."} material-access
  (let [st (store/mem-store)
        material (store/material st "coal-batch-001")]
    (is (some? material) "Should find material")
    (is (= (:supplier material) "supplier-001") "Should link to supplier")
    (is (:verified? material) "Should be verified")))

(deftest ^{:doc "Can retrieve production-run records."} production-run-access
  (let [st (store/mem-store)
        run (store/production-run st "run-001")]
    (is (some? run) "Should find run")
    (is (= (:status run) :scheduled) "Should be scheduled")))

(deftest ^{:doc "Can retrieve emissions report records."} emissions-report-access
  (let [st (store/mem-store)
        report (store/emissions-report st "report-001")]
    (is (some? report) "Should find report")
    (is (= (:date report) "2026-07-14") "Should have correct date")
    (is (:compliant? report) "Should be compliant")))

(deftest ^{:doc "Can check if supplier is verified."} supplier-verification-guard
  (let [st (store/mem-store)]
    (is (store/supplier-verified? st "supplier-001") "Known supplier should be verified")
    (is (not (store/supplier-verified? st "supplier-unknown")) "Unknown supplier should not be verified")))

(deftest ^{:doc "Can check if material is verified."} material-verification-guard
  (let [st (store/mem-store)]
    (is (store/material-verified? st "coal-batch-001") "Known batch should be verified")
    (is (not (store/material-verified? st "coal-unknown")) "Unknown batch should not be verified")))

(deftest ^{:doc "Can check if emissions exceed thresholds."} emissions-threshold-check
  (let [st (store/mem-store)]
    (is (not (store/emissions-exceeds-threshold? st "report-001"))
      "Report within range should not exceed thresholds")))
