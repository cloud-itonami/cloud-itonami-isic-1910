(ns coke.facts
  "Per-jurisdiction coke-oven manufacturing safety and emissions requirements.
  Every jurisdiction in this catalog is backed by an official spec-basis.
  NEVER invent requirements without an official citation.

  South Korea (:KOR), Germany (:DEU), and Poland (:POL) are scoped more
  narrowly than JPN/USA/GBR -- only emissions-monitoring and worker-safety
  are verified and included for each; raw-material-verification and
  byproduct-handling are honestly absent rather than guessed by analogy to
  the other jurisdictions.

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
                    :evidence [:risk-assessment-record :hazard-protective-measures-cert]}}}

   ;; Germany (DEU) -- WebFetch-verified 2026-07-22 directly against
   ;; gesetze-im-internet.de (the Bundesministerium der Justiz's own
   ;; official law-text portal). Emissions citation is HIGH confidence:
   ;; Anhang 1 zur 4. BImSchV (Ordinance on installations requiring a
   ;; permit) Item 1.11 explicitly names "Kokereien" (coking plants) among
   ;; the "Anlagen zur Trockendestillation" requiring a full permit
   ;; procedure with public participation -- read directly, not inferred
   ;; by analogy to a generic industrial-emissions law. Worker-safety
   ;; citation is also HIGH confidence: the Arbeitsschutzgesetz (ArbSchG)
   ;; itself, § 3 (general employer duties) and § 5 (mandatory assessment
   ;; of working conditions / Gefährdungsbeurteilung), read directly --
   ;; the same risk-assessment shape JPN's and KOR's entries already use.
   ;; No raw-material-verification or byproduct-handling entry is made for
   ;; DEU -- this iteration did not find/verify a citation for either, so
   ;; those categories are honestly absent rather than fabricated.
   :DEU
   {:name "Germany"
    :requirements
    {:emissions-monitoring {:description "Permit required before constructing or operating a coking plant, under the ordinance listing installations requiring a permit"
                           :required true
                           :spec-basis "Vierte Verordnung zur Durchführung des Bundes-Immissionsschutzgesetzes (4. BImSchV), Anhang 1, Nr. 1.11 (Anlagen zur Trockendestillation, insbesondere Kokereien) -- full permit procedure with public participation (Verfahrensart G), read directly at gesetze-im-internet.de"
                           :evidence [:installation-permit :ta-luft-compliance-record]}
     :worker-safety {:description "Mandatory assessment of working conditions (Gefährdungsbeurteilung) and general employer duty to ensure occupational safety and health"
                    :required true
                    :spec-basis "Arbeitsschutzgesetz (ArbSchG) § 3 (Grundpflichten des Arbeitgebers) + § 5 (Beurteilung der Arbeitsbedingungen) -- read directly at gesetze-im-internet.de"
                    :evidence [:risk-assessment-record :safety-plan]}}}

   ;; Poland (POL) -- WebFetch/curl-verified 2026-07-22 directly against
   ;; dziennikustaw.gov.pl (the official Journal of Laws of the Republic of
   ;; Poland / Dziennik Ustaw, published by the Rządowe Centrum Legislacji --
   ;; the same official-gazette class of primary source as Germany's
   ;; gesetze-im-internet.de). isap.sejm.gov.pl -- the other official portal,
   ;; which also mirrors these texts -- was tried first but its document
   ;; viewer sits behind an Imperva bot-detection interstitial this
   ;; iteration's tools could not pass (same class of limitation as
   ;; law.go.kr hit for KOR); dziennikustaw.gov.pl's direct PDF URLs served
   ;; the real gazette PDFs with no such block. Emissions citation is HIGH
   ;; confidence: downloaded the actual regulation PDF (Dz.U. 2014 poz. 1169,
   ;; "Rozporządzenie Ministra Środowiska z dnia 27 sierpnia 2014 r. w
   ;; sprawie rodzajów instalacji mogących powodować znaczne zanieczyszczenie
   ;; poszczególnych elementów przyrodniczych albo środowiska jako całości")
   ;; and read its Annex directly with pdftotext: Section 1 ("Instalacje do
   ;; wytwarzania energii i paliw"), item 3, reads verbatim "do produkcji
   ;; koksu" (coke production) -- the same Annex I item 1.3 category used by
   ;; the EU Industrial Emissions Directive 2010/75/EU, which this
   ;; regulation's own footnote states it implements. Installations on this
   ;; list require an integrated permit under Prawo ochrony środowiska
   ;; (Environmental Protection Law) Art. 201, the statute whose Art. 201
   ;; ust. 2 delegation this regulation's own preamble cites as its legal
   ;; basis. Worker-safety citation is also HIGH confidence: downloaded the
   ;; Kodeks pracy (Labour Code) consolidated-text gazette PDF (Dz.U. 2023
   ;; poz. 1465) and read Art. 207 § 1-2 (general employer duty to ensure
   ;; safe and hygienic working conditions) and Art. 226 (mandatory
   ;; assessment and documentation of occupational risk, plus a duty to
   ;; inform workers of that risk) directly with pdftotext -- the same
   ;; risk-assessment shape JPN's/KOR's/DEU's entries already use. No
   ;; raw-material-verification or byproduct-handling entry is made for POL
   ;; -- this iteration did not find/verify a citation for either, so those
   ;; categories are honestly absent rather than fabricated.
   :POL
   {:name "Poland"
    :requirements
    {:emissions-monitoring {:description "Integrated permit required before operating an installation of a type classified as capable of significant pollution, including coke-production installations"
                           :required true
                           :spec-basis "Rozporządzenie Ministra Środowiska z dnia 27 sierpnia 2014 r. w sprawie rodzajów instalacji mogących powodować znaczne zanieczyszczenie poszczególnych elementów przyrodniczych albo środowiska jako całości (Dz.U. 2014 poz. 1169), Załącznik, ust. 1 pkt 3 (\"do produkcji koksu\") -- implements EU Industrial Emissions Directive 2010/75/EU Annex I 1.3; installations on the list require an integrated permit (pozwolenie zintegrowane) under Prawo ochrony środowiska Art. 201 -- read directly at dziennikustaw.gov.pl"
                           :evidence [:integrated-permit :installation-classification-record]}
     :worker-safety {:description "General employer duty to ensure safe and hygienic working conditions, plus mandatory assessment and documentation of occupational risk"
                    :required true
                    :spec-basis "Kodeks pracy (Labour Code), Art. 207 § 1-2 (podstawowe obowiązki pracodawcy w zakresie bezpieczeństwa i higieny pracy) + Art. 226 (ocena i dokumentowanie ryzyka zawodowego oraz informowanie pracowników) -- consolidated text Dz.U. 2023 poz. 1465, read directly at dziennikustaw.gov.pl"
                    :evidence [:risk-assessment-record :safety-plan]}}}})

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
