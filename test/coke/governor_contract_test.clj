(ns coke.governor-contract-test
  (:require [clojure.test :refer [deftest is]]
            [coke.store :as store]
            [coke.advisor :as advisor]
            [coke.governor :as governor]
            [coke.registry :as registry]))

(deftest spec-basis-hard-gate
  "Spec-basis is a HARD gate: never allow proposals without official citations."
  (let [st (store/mem-store)
        proposal {:op :actuation/schedule-production-run
                  :subject "run-001"
                  :effect :propose
                  :value {:evidence {:coal-batch-verified true}
                          :confidence 0.9}
                  :cites []}]
    (let [eval (governor/evaluate proposal st)]
      (is (:holds? eval) "Proposal with empty cites should hold")
      (is (seq (:hard-violations eval)) "Should have hard violations")
      (is (some #(= (:rule %) :no-spec-basis) (:hard-violations eval))))))

(deftest process-control-block
  "HARD BLOCK: Proposals mentioning furnace control, charging, or process
  parameters are immediately rejected. Those remain engineer exclusive authority."
  (let [st (store/mem-store)
        proposal {:op :actuation/schedule-production-run
                  :subject "run-001"
                  :effect :propose
                  :cites ["some-spec"]
                  :value {:evidence {:coal-batch-verified true}
                          :confidence 0.9
                          :detail "Please set furnace temperature to 900C and charge now"}}]
    (let [eval (governor/evaluate proposal st)]
      (is (:holds? eval) "Process-control proposal should hold")
      (is (some #(= (:rule %) :process-control-forbidden) (:hard-violations eval))
        "Should have process-control-forbidden violation"))))

(deftest emissions-threshold-exceedance-escalation
  "Emissions reports with threshold exceedances ALWAYS escalate to human.
  Never silently log a threshold exceedance."
  (let [st (store/mem-store)
        proposal {:op :actuation/log-emissions-report
                  :subject "report-001"
                  :effect :propose
                  :cites ["Air Pollution Control Law §3"]
                  :value {:evidence {:monitoring-data true}
                          :confidence 0.95
                          :threshold-exceeded? true
                          :detail "SO2 exceeds threshold"}}]
    (let [eval (governor/evaluate proposal st)]
      (is (:holds? eval) "Threshold exceedance should hold")
      (is (some #(= (:rule %) :emissions-threshold-exceedance) (:hard-violations eval))
        "Should have emissions-threshold-exceedance violation"))))

(deftest actuation-requires-escalation
  "Both production scheduling and emissions reporting require human sign-off,
  even when all other checks are clean."
  (let [st (store/mem-store)
        adv (advisor/mock-advisor)
        prod-proposal (advisor/production-proposal adv "run-001")]
    (let [eval (governor/evaluate prod-proposal st)]
      (is (seq (:soft-violations eval)) "Should have soft violations for actuation")
      (is (some #(= (:rule %) :escalate) (:soft-violations eval))
        "Should escalate high-stakes actuation"))))

(deftest supplier-not-verified-blocks-intake
  "Raw material intake from unverified supplier is blocked."
  (let [st (store/mem-store)
        proposal (registry/intake-draft "supplier-unknown"
                   ["Mining Safety Regulation §24"]
                   {:supplier-license true}
                   0.85
                   "Coal from unknown supplier")]
    (let [eval (governor/evaluate proposal st)]
      (is (seq (:hard-violations eval)) "Should have hard violations")
      (is (some #(= (:rule %) :supplier-not-verified) (:hard-violations eval))
        "Should block unverified supplier"))))

(deftest material-not-verified-blocks-production
  "Production-run scheduling with unverified coal batch is blocked."
  (let [st (store/mem-store)
        ;; Create a production run with an unverified coal batch
        _ (swap! (-> st :data) assoc-in [:production-runs "run-002" :coal-batch] "coal-unknown")
        proposal (registry/schedule-production-draft "run-002"
                   ["Industrial Safety and Health Act §20"]
                   {:emissions-baseline true}
                   0.88
                   "Schedule production")]
    (let [eval (governor/evaluate proposal st)]
      (is (seq (:hard-violations eval)) "Should have hard violations")
      (is (some #(= (:rule %) :material-not-verified) (:hard-violations eval))
        "Should block unverified material"))))

(deftest low-confidence-escalates
  "Low confidence proposals escalate to human, even if otherwise clean."
  (let [st (store/mem-store)
        proposal {:op :actuation/log-emissions-report
                  :subject "report-001"
                  :effect :propose
                  :cites ["Air Pollution Control Law §3"]
                  :value {:evidence {:monitoring-data true}
                          :confidence 0.45
                          :threshold-exceeded? false
                          :detail "Emissions within range"}}]
    (let [eval (governor/evaluate proposal st)]
      (is (seq (:soft-violations eval)) "Should have soft violations")
      (is (some #(= (:rule %) :escalate) (:soft-violations eval))
        "Should escalate low-confidence"))))

(deftest clean-proposal
  "A proposal with all evidence, valid spec-basis, high confidence,
  and no high-stakes actuation or process-control is clean."
  (let [st (store/mem-store)
        proposal {:op :proposal/coordinate-byproduct-shipment
                  :subject "shipment-001"
                  :effect :propose
                  :cites ["Industrial Safety and Health Act §21"]
                  :value {:evidence {:storage-facility-cert true :hazmat-procedure true}
                          :confidence 0.9
                          :detail "Byproduct shipment to licensed facility"}}]
    (let [eval (governor/evaluate proposal st)]
      (is (:clean? eval) "Should be clean")
      (is (empty? (:hard-violations eval)) "Should have no hard violations"))))
