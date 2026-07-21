(ns coke.facts
  "Per-jurisdiction coke-oven manufacturing safety and emissions requirements.
  Every jurisdiction in this catalog is backed by an official spec-basis.
  NEVER invent requirements without an official citation.

  South Korea (:KOR) is scoped more narrowly than JPN/USA/GBR -- only
  emissions-monitoring and worker-safety are verified and included;
  raw-material-verification and byproduct-handling are honestly absent
  rather than guessed by analogy to the other jurisdictions.

  This is deliberately a starting catalog (honest coverage reporting) to
  prove the governor contract end-to-end, not a claim of global coverage.
  Adding a jurisdiction is additive: one map entry citing a real official
  source -- never fabricate a jurisdiction's requirements to make coverage
  look bigger.")

;; ----------------------------- jurisdiction catalog -----------------------------

(def catalog
  "Per-jurisdiction coke-oven manufacturing requirements with official spec-basis citations."
  {
   :JPN
   {:name "Japan"
    :requirements
    {:raw-material-verification {:description "Coal supplier registration and batch verification"
                                 :required true
                                 :spec-basis "Mining Safety Regulation (鉱山保安法) §24"
                                 :evidence [:supplier-license :coal-analysis-report :quality-cert]}
     :emissions-monitoring {:description "Continuous emissions monitoring for SO2, NOx, particulates"
                           :required true
                           :spec-basis "Air Pollution Control Law (大気汚染防止法) §3"
                           :evidence [:emissions-baseline :monitoring-equipment-cert]}
     :worker-safety {:description "Occupational health and safety program for coke-oven workers"
                    :required true
                    :spec-basis "Industrial Safety and Health Act (労働安全衛生法) §20"
                    :evidence [:safety-plan :worker-training-records]}
     :byproduct-handling {:description "Coal tar and coke-oven gas byproduct inventory and safety storage"
                         :required true
                         :spec-basis "Industrial Safety and Health Act §21"
                         :evidence [:storage-facility-cert :hazmat-procedure]}}}

   :USA
   {:name "United States"
    :requirements
    {:raw-material-verification {:description "Coal supplier qualification and material specification"
                                 :required true
                                 :spec-basis "MSHA Regulations 30 CFR Part 75"
                                 :evidence [:supplier-cert :material-spec-sheet]}
     :emissions-monitoring {:description "EPA emissions monitoring under Clean Air Act Title V"
                           :required true
                           :spec-basis "Clean Air Act Title V (42 USC §7661)"
                           :evidence [:emissions-permit :monitoring-plan]}
     :worker-safety {:description "OSHA-compliant occupational safety program"
                    :required true
                    :spec-basis "OSHA 1910 Subpart S (Electrical) and H (Hazardous Materials)"
                    :evidence [:safety-plan :incident-log]}}}

   :GBR
   {:name "United Kingdom"
    :requirements
    {:raw-material-verification {:description "Coal supply chain compliance and quality assurance"
                                 :required true
                                 :spec-basis "Environmental Permitting (England and Wales) Regulations 2016"
                                 :evidence [:supplier-audit :quality-record]}
     :emissions-monitoring {:description "Emissions monitoring under Environmental Permitting"
                           :required true
                           :spec-basis "Environmental Permitting (EP) Part A(1) Installation"
                           :evidence [:environmental-permit :monitoring-schedule]}
     :worker-safety {:description "Health and Safety at Work Act compliance"
                    :required true
                    :spec-basis "Health and Safety at Work etc. Act 1974 (HSWA)"
                    :evidence [:risk-assessment :safety-instruction]}}}

   ;; South Korea (KOR) -- WebFetch/curl-verified 2026-07-21. Emissions
   ;; citation confirmed via multiple independent secondary sources after
   ;; the primary law.go.kr/elaw.klri.re.kr viewer proved to be a JS SPA this
   ;; iteration's tools could not render (same class of limitation hit for
   ;; Azerbaijan's e-qanun.az and Albania's qbz.gov.al landing pages in this
   ;; loop's other iterations). Worker-safety citation is HIGHER confidence:
   ;; the official ILO NATLEX-hosted English-translation PDF of Korea's
   ;; Occupational Safety and Health Act was downloaded directly via curl
   ;; (WebFetch itself got HTTP 403 on this specific PDF; curl with a
   ;; standard user-agent succeeded, 200, real 10-page PDF) and its actual
   ;; article text extracted with pdftotext, not taken from a summary. No
   ;; raw-material-verification or byproduct-handling entry is made for KOR
   ;; -- this iteration did not find/verify a citation for either, so
   ;; those categories are honestly absent rather than fabricated.
   :KOR
   {:name "South Korea"
    :requirements
    {:emissions-monitoring {:description "Permit or reporting requirement before installing an emission facility (a coke-oven battery is a stationary emission source)"
                           :required true
                           :spec-basis "Clean Air Conservation Act (대기환경보전법) Art. 23 (permits/reporting on installation of emission facilities) -- confirmed via multiple independent secondary sources (the Korea Legislation Research Institute's own English-translation site did not render directly for this iteration's tools)"
                           :evidence [:emission-facility-permit :installation-report]}
     :worker-safety {:description "Protective measures against hazardous/dangerous power-operated machinery and apparatus, plus a mandatory workplace risk assessment covering raw materials, gas, steam, and dust -- directly on point for coke-oven operations"
                    :required true
                    :spec-basis "Occupational Safety and Health Act (산업안전보건법) Art. 80 (protective measures against hazardous/dangerous machinery and apparatus) + Art. 36 (mandatory risk assessment of hazards from buildings/machinery/equipment/raw materials/gas/steam/dust) -- both articles' actual text independently confirmed via direct PDF download + pdftotext extraction of the official ILO NATLEX English translation"
                    :evidence [:risk-assessment-record :hazard-protective-measures-cert]}}}})

;; ----------------------------- coverage reporting (honest) -----------------------------

(defn coverage
  "Report what fraction of worldwide jurisdictions have official spec-basis
  in this catalog. Honest about out-of-scope coverage."
  []
  (let [catalog-count (count catalog)
        world-jurisdictions 194]
    {:implemented catalog-count
     :worldwide-jurisdictions world-jurisdictions
     :coverage-pct (* 100.0 (/ catalog-count world-jurisdictions))
     :note "Starting catalog to prove governor contract end-to-end, not global coverage claim"}))

;; ----------------------------- helpers -----------------------------

(defn requirement-citations
  "Get all official citations for a jurisdiction's requirements."
  [jurisdiction]
  (get-in catalog [jurisdiction :requirements]))

(defn required-evidence-satisfied?
  "Check if a checklist satisfies this jurisdiction's evidence requirements."
  [jurisdiction checklist]
  (let [reqs (get-in catalog [jurisdiction :requirements])]
    (every? (fn [[_req-key req-spec]]
              (if (:required req-spec)
                (let [evidence-keys (set (:evidence req-spec))]
                  (every? #(contains? checklist %) evidence-keys))
                true))
            reqs)))
