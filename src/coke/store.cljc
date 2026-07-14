(ns coke.store
  "In-memory store for coke-plant operations state.
  This is a reference implementation; production systems would use Datomic
  or similar persistent event store for audit and replay.")

;; ----------------------------- store initialization -----------------------------

(defn mem-store
  "Create an in-memory store with reference data.
  Hospital and critical-infrastructure meters are marked protected-recipient?."
  []
  {:data (atom {
           :suppliers {
             "supplier-001" {:name "Coal Mining Corp A"
                            :license-verified? true
                            :jurisdiction :JPN}
             "supplier-002" {:name "Industrial Coal Ltd"
                            :license-verified? true
                            :jurisdiction :USA}}
           :materials {
             "coal-batch-001" {:supplier "supplier-001"
                              :quality-grade "high-volatile-bituminous"
                              :verified? true}}
           :production-runs {
             "run-001" {:status :scheduled
                       :coal-batch "coal-batch-001"
                       :scheduled-start "2026-07-15T09:00:00Z"}}
           :emissions-reports {
             "report-001" {:date "2026-07-14"
                          :so2-ppm 0.8
                          :nox-ppm 1.2
                          :threshold-so2 1.0
                          :threshold-nox 1.5
                          :compliant? true}}})})

;; ----------------------------- accessors -----------------------------

(defn supplier
  "Get supplier record by ID."
  [st supplier-id]
  (get-in @(:data st) [:suppliers supplier-id]))

(defn material
  "Get material (coal batch) record by ID."
  [st material-id]
  (get-in @(:data st) [:materials material-id]))

(defn production-run
  "Get production run record by ID."
  [st run-id]
  (get-in @(:data st) [:production-runs run-id]))

(defn emissions-report
  "Get emissions report by ID."
  [st report-id]
  (get-in @(:data st) [:emissions-reports report-id]))

;; ----------------------------- guards -----------------------------

(defn supplier-verified?
  "Check if supplier license is verified."
  [st supplier-id]
  (let [s (supplier st supplier-id)]
    (:license-verified? s false)))

(defn material-verified?
  "Check if material batch is verified."
  [st material-id]
  (let [m (material st material-id)]
    (:verified? m false)))

(defn emissions-exceeds-threshold?
  "Check if emissions report shows any threshold exceedance."
  [st report-id]
  (let [r (emissions-report st report-id)]
    (or (> (:so2-ppm r 0) (:threshold-so2 r 1.0))
        (> (:nox-ppm r 0) (:threshold-nox r 1.5)))))
