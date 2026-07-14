(ns coke.sim
  "Simulation harness for Coke Operations Coordinator actor.
  Run with: clojure -M:dev:run"
  (:require [coke.advisor :as advisor]
            [coke.governor :as governor]
            [coke.store :as store]))

(defn -main
  "Drive a simple coke-oven operations workflow through the governor."
  [& _args]
  (let [st (store/mem-store)
        adv (advisor/mock-advisor)

        ;; Scenario 1: Raw material intake (clean proposal)
        intake-proposal (advisor/intake-proposal adv "supplier-001")
        intake-eval (governor/evaluate intake-proposal st)

        ;; Scenario 2: Production-run scheduling (clean proposal, but high-stakes)
        prod-proposal (advisor/production-proposal adv "run-001")
        prod-eval (governor/evaluate prod-proposal st)

        ;; Scenario 3: Emissions report (threshold exceedance, hard escalation)
        emissions-proposal (advisor/emissions-proposal adv "report-001" true)
        emissions-eval (governor/evaluate emissions-proposal st)]

    (println "=== COKE OPERATIONS COORDINATOR SIMULATION ===\n")

    (println "--- Scenario 1: Raw Material Intake ---")
    (println "Proposal:" intake-proposal)
    (println "Evaluation:" intake-eval)
    (println "Result:" (if (:clean? intake-eval) "APPROVED" "ESCALATE TO HUMAN"))
    (println)

    (println "--- Scenario 2: Production-Run Scheduling ---")
    (println "Proposal:" prod-proposal)
    (println "Evaluation:" prod-eval)
    (println "Result:" (if (:holds? prod-eval) "HOLD - Hard violations" "ESCALATE - High-stakes actuation"))
    (println)

    (println "--- Scenario 3: Emissions Report (Threshold Exceedance) ---")
    (println "Proposal:" emissions-proposal)
    (println "Evaluation:" emissions-eval)
    (println "Hard Violations:" (:hard-violations emissions-eval))
    (println "Result:" (if (:holds? emissions-eval) "HARD BLOCK - Escalate immediately" "ERROR"))
    (println)))
