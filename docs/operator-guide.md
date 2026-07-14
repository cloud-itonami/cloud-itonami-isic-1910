# Operator Guide: Coke Oven Operations Coordinator

## Quick Start

### Prerequisites

- Clojure CLI (`clojure` command)
- Java 11+
- Git

### Running Tests

```bash
clojure -M:test
```

Expected output: All tests green, zero failures.

### Running the Simulation

```bash
clojure -M:dev:run
```

Output: Demo scenarios showing advisor proposals and governor evaluations.

## Understanding the Actor Flow

```
1. Advisor proposes an operation (intake, scheduling, reporting)
2. Governor evaluates against hard and soft gates
3. If hard violations: HOLD (escalate to human)
4. If clean or only soft violations: ESCALATE (human reviews)
5. Human approves/rejects
6. If approved: Record to audit ledger, dispatch robot missions
7. If rejected: Return to advisor for refinement
```

## Operations Workflow

### 1. Raw Material Intake

**Advisor Proposes:** Coal batch from supplier X, quality-grade Y, ready for coking

**Governor Checks:**
- Is supplier license verified? (hard gate)
- Is coal batch verified? (hard gate)
- Evidence checklist complete? (hard gate)
- Confidence > 0.6? (soft gate)

**Outcome:** If clean, record to ledger; if holds or low confidence, escalate to human.

### 2. Production-Run Scheduling

**Advisor Proposes:** Schedule production run for coal batch X, start time T

**Governor Checks:**
- Spec-basis citation present? (hard gate)
- Coal batch verified? (hard gate)
- Emissions baseline recorded? (hard gate)
- No process-control keywords? (hard gate)
- High-stakes actuation requires human sign-off (soft gate)

**Outcome:** Always escalates to human (high-stakes). Human plant engineer approves, then real furnace scheduling happens (NOT by actor).

### 3. Byproduct Shipment Coordination

**Advisor Proposes:** Ship coal tar batch Z to licensed facility F

**Governor Checks:**
- Spec-basis citation present? (hard gate)
- Facility licensed and verified? (hard gate)
- Hazmat procedures documented? (hard gate)

**Outcome:** If clean, record and dispatch coordination robot; if violations, escalate.

### 4. Emissions Compliance Reporting

**Advisor Proposes:** Record monitoring data: SO2=0.8 ppm, NOx=1.2 ppm, baseline=1.0/1.5

**Governor Checks:**
- Spec-basis citation present? (hard gate)
- Monitoring equipment certified? (hard gate)
- **Threshold exceeded?** (hard gate: if yes, ALWAYS hold)

**Outcome:** If threshold exceeded, HARD BLOCK (escalate immediately, never silently log). If compliant, record to ledger.

## Key Safety Properties

### Furnace Control is Forbidden

Any proposal mentioning furnace charging, discharging, temperature setpoints, or process parameters is immediately rejected. Example:

```
Proposal: "Schedule production with furnace charge at 800C for 20 hours"
Governor Evaluation: HARD VIOLATION :process-control-forbidden
Result: HOLD (escalate to human)
```

Plant engineers make all process-control decisions.

### Emissions Threshold Exceedance Cannot Be Silent

If emissions monitoring shows any value exceeding its jurisdiction's threshold, the actor MUST escalate to human. It cannot record a threshold-exceeded report silently.

Example:

```
Proposal: Log emissions report (SO2=1.5 ppm, threshold=1.0)
Governor Evaluation: HARD VIOLATION :emissions-threshold-exceedance
Result: HOLD (escalate immediately)
Human reviews and decides: investigate cause, reduce load, extend coking time, etc.
```

### Supplier/Material Verification is Mandatory

No production can schedule if the coal supplier is not registered or the specific batch is not verified. This prevents contaminated or off-spec coal from entering the furnace.

## Jurisdictional Coverage

| Jurisdiction | Implemented | Source |
|--------------|-------------|--------|
| Japan        | Yes         | Gas Utility Regulation Act, Industrial Safety and Health Act, Air Pollution Control Law |
| USA          | Yes         | MSHA, Clean Air Act, OSHA |
| UK           | Yes         | Environmental Permitting Regulations, Health and Safety at Work Act |

**To add a jurisdiction:** Submit a pull request with:
1. Jurisdiction code (e.g., `:AUS` for Australia)
2. Official requirement citations (do not invent)
3. Evidence checklist for each requirement
4. Tests verifying coverage

## Customization Examples

### Adding a New Requirement

Edit `src/coke/facts.cljc`:

```clojure
:JPN
{:name "Japan"
 :requirements
 {;; ... existing requirements ...
  :new-requirement {:description "Worker training for coke-oven operations"
                    :required true
                    :spec-basis "Industrial Safety and Health Act §25"
                    :evidence [:training-cert :competency-assessment]}}}
```

Run tests to verify:

```bash
clojure -M:test
```

### Customizing Confidence Floor

Edit `src/coke/governor.cljc`:

```clojure
(def confidence-floor 0.75)  ;; Raise from 0.6 to 0.75 for stricter gating
```

Re-run tests and simulation.

### Integrating Real Coal-Supply Vendor

Edit `src/coke/store.cljc` to fetch supplier data from your vendor's API instead of hardcoded reference data.

## Troubleshooting

### "Proposal with empty cites should hold"

Your proposal lacks a `:cites` field with jurisdiction-specific spec-basis citations. Add:

```clojure
:cites ["Gas Utility Regulation Act §24"]
```

### "Process-control proposal should hold"

Your proposal mentions a forbidden keyword (furnace, charge, temperature, etc.). Remove process-control language and delegate those decisions to plant engineers.

### "Threshold exceedance should hold"

Your emissions proposal has `:threshold-exceeded? true`. This ALWAYS hard-blocks. Investigate the cause, reduce load, or extend coking time, then resubmit with `:threshold-exceeded? false`.

## Audit and Compliance

All decisions are recorded to the audit ledger (Datomic in production). Example query:

```clojure
;; Find all emissions reports with threshold exceedances
(q '[:find ?time ?report ?detail
     :where
     [?e :operation :log-emissions-report]
     [?e :timestamp ?time]
     [?e :subject ?report]
     [?e :threshold-exceeded? true]
     [?e :detail ?detail]]
   db)
```

Regulators can inspect:
- Decision timestamp
- Advisor confidence level
- Governor violation reason
- Human approver identity
- Real-world actuation outcome (if any)

## Support

- **Questions:** See README.md and GOVERNANCE.md
- **Bug Reports:** GitHub Issues
- **Contributions:** See CONTRIBUTING.md
