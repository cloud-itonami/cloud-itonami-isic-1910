(ns coke.advisor
  "Coke Operations Advisor -- the LLM-driven suggestion layer.
  Proposes operations to the Governor for approval.")

;; ----------------------------- mock advisor for testing -----------------------------

(defn mock-advisor
  "Create a mock advisor for testing. Real implementation would call an LLM."
  []
  {:type :mock :model "mock-v1"})

(defn intake-proposal
  "Propose a raw material intake operation."
  [_advisor supplier-id]
  {:op :proposal/intake-raw-material
   :subject supplier-id
   :effect :propose
   :cites ["Mining Safety Regulation (鉱山保安法) §24"]
   :value {:evidence {:supplier-license true :coal-analysis-report true :quality-cert true}
           :confidence 0.85
           :detail "Coal batch verified from approved supplier"}})

(defn production-proposal
  "Propose a production-run scheduling operation."
  [_advisor run-id]
  {:op :actuation/schedule-production-run
   :subject run-id
   :effect :propose
   :cites ["Industrial Safety and Health Act (労働安全衛生法) §20"]
   :value {:evidence {:coal-batch-verified true :emissions-baseline true :safety-plan true}
           :confidence 0.88
           :detail "Production run ready to schedule"}})

(defn emissions-proposal
  "Propose an emissions report (may include threshold exceedances)."
  [_advisor report-id threshold-exceeded?]
  {:op :actuation/log-emissions-report
   :subject report-id
   :effect :propose
   :cites ["Air Pollution Control Law (大気汚染防止法) §3"]
   :value {:evidence {:monitoring-data true :calibration-cert true}
           :confidence 0.92
           :threshold-exceeded? threshold-exceeded?
           :detail (if threshold-exceeded?
                    "Emissions monitoring: SO2 exceeds threshold, escalation required"
                    "Emissions within compliance range")}})
